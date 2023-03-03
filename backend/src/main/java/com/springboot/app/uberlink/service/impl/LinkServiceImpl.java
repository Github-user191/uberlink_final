package com.springboot.app.uberlink.service.impl;

import com.google.common.hash.Hashing;
import com.springboot.app.uberlink.exception.exceptions.LinkException;
import com.springboot.app.uberlink.model.Link;
import com.springboot.app.uberlink.model.User;
import com.springboot.app.uberlink.repository.LinkRepository;
import com.springboot.app.uberlink.repository.UserRepository;
import com.springboot.app.uberlink.service.LinkService;
import lombok.AllArgsConstructor;
import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.List;

@Service
@AllArgsConstructor
public class LinkServiceImpl implements LinkService {

    private final LinkRepository linkRepository;
    private final UserRepository userRepository;

//    @Cacheable(value="Link", key="#shortenedLink")
    @Override
    public Link getOriginalLink(String shortenedLink, String emailAddress) {
        User user = userRepository.findUserByEmailAddress(emailAddress);

        Link link = linkRepository.findByShortenedLinkAndLinkCreatorEmailAddress(shortenedLink, user.getEmailAddress());

        if(link==null) {
            throw new LinkException("Whoops! This Uberlink does not exist");
        }

        if(link.isLinkExpired() || !link.isActive()) {
            throw new LinkException("Whoops! This Uberlink does not exist or has expired");
        }


        return link;

    }

    @Override
    public Link createShortenedLink(String originalLink, String emailAddress) {

        User user = userRepository.findUserByEmailAddress(emailAddress);

        if(!isUrlValid(originalLink)) {
            throw new LinkException("Link invalid, try again");
        }

        Link linkObj = new Link();
        linkObj.setOriginalLink(originalLink);
        linkObj.setShortenedLink(getShortUrl(originalLink));
        linkObj.setActive(true);
        linkObj.setLinkCreatorEmailAddress(user.getEmailAddress());

        linkObj.setUser(user);



        if(linkRepository.existsByOriginalLinkAndLinkCreatorEmailAddressEquals(originalLink, user.getEmailAddress())) {
            return linkRepository.findByOriginalLinkAndLinkCreatorEmailAddress(originalLink, user.getEmailAddress());
        }


        userRepository.save(user);

        return linkRepository.save(linkObj);
    }

//    @CacheEvict(value="Link", key="#shortenedLink")
    @Override
    public Boolean deleteByShortenedLink(String shortenedLink, String emailAddress) {

        Link link = linkRepository.findByShortenedLinkAndLinkCreatorEmailAddress(shortenedLink, emailAddress);

        if(link == null) {
            return false;
        }

        linkRepository.delete(link);
        return true;
    }

    @Override
    public Page<Link> getAllUserLinksByEmailAddress(String emailAddress, Pageable pageable) {
        Page<Link> userLinks = linkRepository.findAllByLinkCreatorEmailAddress(emailAddress, pageable);

        if(userLinks.isEmpty()) {
            throw new LinkException("User has no Uberlinks yet");
        }

        userLinks.stream().forEach(link -> {
            if (link.isLinkExpired()) {
                link.setActive(false);
                linkRepository.save(link);
            }
        });


        return userLinks;
    }

//    @Cacheable(value = "Link")
    @Override
    public Page<Link> getAllUserActiveLinksByEmailAddress(String emailAddress, Pageable pageable) {
        Page<Link> userLinks = linkRepository.findAllByLinkCreatorEmailAddressAndActiveIsTrue(emailAddress, pageable);

        if(userLinks.isEmpty()) {
            throw new LinkException("User has no Uberlinks yet");
        }

        userLinks.stream().forEach(link -> {
            if (link.isLinkExpired()) {
                link.setActive(false);
                linkRepository.save(link);
            }

        });
        return userLinks;
    }

    @Override
    public Boolean expireShortenedLink(String emailAddress, String shortenedLink) {
        Link link = linkRepository.findByShortenedLinkAndLinkCreatorEmailAddress(shortenedLink, emailAddress);

        if(link.isLinkExpired() || !link.isActive()) {
            return false;
        }

        link.setActive(false);


        linkRepository.save(link);

        return true;
    }


    private String getShortUrl(String url) {
        return Hashing.murmur3_32_fixed().hashString(url, Charset.defaultCharset()).toString();
    }

    private boolean isUrlValid(String url) {
        UrlValidator urlValidator = new UrlValidator(new String[]{"http","https"});
        return urlValidator.isValid(url);
    }
}
