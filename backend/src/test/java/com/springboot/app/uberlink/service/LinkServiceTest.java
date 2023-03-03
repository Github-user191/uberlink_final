package com.springboot.app.uberlink.service;

import com.springboot.app.uberlink.exception.exceptions.LinkException;
import com.springboot.app.uberlink.model.Link;
import com.springboot.app.uberlink.model.User;
import com.springboot.app.uberlink.repository.LinkRepository;
import com.springboot.app.uberlink.repository.UserRepository;
import com.springboot.app.uberlink.service.impl.LinkServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class LinkServiceTest {

    // @Mock - Creates a mock of a dependency
    // @InjectMocks - Creates an instance of the class and injects the required dependencies/mocks that are created using @Mock into the instance


    @InjectMocks
    private LinkServiceImpl linkService;

    @Mock
    private LinkRepository linkRepository;

    @Mock
    private UserRepository userRepository;

    private Link link1, link2;

    private User user;

    @BeforeEach
    void setUp() {
        linkService = new LinkServiceImpl(linkRepository, userRepository);

        user = new User(1L, "John Doe", "johndoe@gmail.com", "password");
        link1 = new Link(1L, "uberlink.tech/5563JhdZ", "https://www.youtube.com/watch?3=393dkodwkd3", true, "john@gmail.com", user);
        link2 = new Link(2L, "uberlink.tech/cCu887Sj", "https://www.facebook/com?post=288333", true, "john@gmail.com", user);


    }

    @Test
    public void testCanGetOriginalLink() {

        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);
        when(linkRepository.findByShortenedLinkAndLinkCreatorEmailAddress(anyString(), anyString()))
                .thenReturn(link1);

        Link theLink = linkService.getOriginalLink(link1.getShortenedLink(), link1.getLinkCreatorEmailAddress());

        assertEquals(link1.getOriginalLink(), theLink.getOriginalLink());
    }


    @Test
    public void willThrowIfLinkIsExpiredOrInactiveWhenGettingOriginalLink() {

        link1.setActive(false);
        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);
        when(linkRepository.findByShortenedLinkAndLinkCreatorEmailAddress(anyString(), anyString()))
                .thenReturn(link1);


        assertThatThrownBy(() -> linkService.getOriginalLink(link1.getShortenedLink(), link1.getLinkCreatorEmailAddress()))
            .isInstanceOf(LinkException.class)
            .hasMessageContaining("Whoops! This Uberlink does not exist or has expired");
    }

    @Test
    public void testCanCreateShortenedLink() {
        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);



        when(linkRepository.save(any())).thenReturn(link1);
        Link createdLink = linkService.createShortenedLink("https://www.javatpoint.com/php-functiodns", link1.getLinkCreatorEmailAddress());

        assertNotNull(createdLink);
        verify(linkRepository).save(any());

    }

    @Test
    public void willThrowIfLinkIsInvalidWhenCreatingShortenedLink() {
        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);

        assertThatThrownBy(() -> linkService.createShortenedLink("https:\\invalid_link", link1.getLinkCreatorEmailAddress()))
                .isInstanceOf(LinkException.class)
                .hasMessageContaining("Link invalid, try again");

        verify(linkRepository, never()).save(any());

    }

    @Test
    public void testCanDeleteByShortenedLink() {
        when(linkRepository.findByShortenedLinkAndLinkCreatorEmailAddress(link1.getShortenedLink(), link1.getLinkCreatorEmailAddress())).thenReturn(link1);

        linkService.deleteByShortenedLink(link1.getShortenedLink(), link1.getLinkCreatorEmailAddress());


        verify(linkRepository).delete(any());
    }

    @Test
    public void testCanGetAllUserLinksByEmailAddress() {

        link1.setActive(false);
        Page<Link> linkPage = new PageImpl<>(List.of(link1, link2));


        when(linkRepository.findAllByLinkCreatorEmailAddress(any(), any())).thenReturn(linkPage);

        Page<Link> links = linkService.getAllUserLinksByEmailAddress(user.getEmailAddress(), PageRequest.of(0,10));


        assertEquals(2, links.getTotalElements());

    }



    @Test
    @Order(1)
    public void testShouldSetLinkInactiveIfLinkIsExpired() {

        link1.setExpiredAt(LocalDateTime.now());
        Page<Link> linkPage = new PageImpl<>(List.of(link1, link2));


        when(linkRepository.findAllByLinkCreatorEmailAddress(any(), any())).thenReturn(linkPage);

        Page<Link> links = linkService.getAllUserLinksByEmailAddress(user.getEmailAddress(), PageRequest.of(0,10));
        System.out.println(links.getContent());

        System.out.println(link1);
        assertFalse(link1.isActive());

        verify(linkRepository).save(any());
    }


    @Test
    public void testCanGetAllActiveUserLinksByEmailAddress() {

        Page<Link> linkPage = new PageImpl<>(List.of(link1, link2));


        when(linkRepository.findAllByLinkCreatorEmailAddress(any(), any())).thenReturn(linkPage);

        Page<Link> links = linkService.getAllUserLinksByEmailAddress(user.getEmailAddress(), PageRequest.of(0,10));

        assertEquals(2, links.getTotalElements());
        assertTrue(link1.isActive());
        assertTrue(link2.isActive());

    }


    @Test
    public void testCanExpireShortenedLink() {

        when(linkRepository.findByShortenedLinkAndLinkCreatorEmailAddress(anyString(), anyString())).thenReturn(link1);

        boolean result = linkService.expireShortenedLink(link1.getLinkCreatorEmailAddress(), link1.getShortenedLink());

        assertTrue(result);
        assertFalse(link1.isActive());
        verify(linkRepository).save(any());

    }

    @Test
    public void willThrowIfLinkIsExpiredOrInactiveWhenExpiringShortenedLink() {

        link1.setActive(false);
        when(linkRepository.findByShortenedLinkAndLinkCreatorEmailAddress(anyString(), anyString())).thenReturn(link1);

        boolean result = linkService.expireShortenedLink(link1.getLinkCreatorEmailAddress(), link1.getShortenedLink());

        assertFalse(result);
        assertFalse(link1.isActive());
        verify(linkRepository, never()).save(any());
    }



}
