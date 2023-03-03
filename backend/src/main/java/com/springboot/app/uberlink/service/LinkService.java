package com.springboot.app.uberlink.service;

import com.springboot.app.uberlink.model.Link;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LinkService {
    Link getOriginalLink(String shortenedLink, String emailAddress);
    Link createShortenedLink(String originalLink, String emailAddress);
    Boolean deleteByShortenedLink(String shortenedLink, String emailAddress);
    Page<Link> getAllUserLinksByEmailAddress(String emailAddress, Pageable pageable);
    Page<Link> getAllUserActiveLinksByEmailAddress(String emailAddress, Pageable pageable);
    Boolean expireShortenedLink(String emailAddress, String shortenedLink);

}
