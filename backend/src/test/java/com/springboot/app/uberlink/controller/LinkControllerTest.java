package com.springboot.app.uberlink.controller;

import com.springboot.app.uberlink.model.Link;
import com.springboot.app.uberlink.model.User;
import com.springboot.app.uberlink.service.LinkService;
import com.springboot.app.uberlink.service.impl.ErrorValidationServiceImpl;
import com.springboot.app.uberlink.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static com.springboot.app.uberlink.utils.TestUtils.mapToJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = LinkController.class)
public class LinkControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LinkService linkService;

    @MockBean
    private ErrorValidationServiceImpl errorValidationService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    private User user;

    private Link link1, link2;

    @BeforeEach
    void setUp() {
        user = new User(1L, "John Doe", "johndoe@gmail.com", "password");
        link1 = new Link(1L, "uberlink.online/5563JhdZ", "https://www.youtube.com/watch?3=393dkodwkd3", true, "john@gmail.com", user);
        link2 = new Link(2L, "uberlink.online/cCu887Sj", "https://www.facebook/com?post=288333", true, "john@gmail.com", user);
    }

    @WithMockUser(username = "admin", password = "password")
    @Test
    void shouldGetOriginalLinkByShortenedLink() throws Exception {

        when(linkService.getOriginalLink(any(), any())).thenReturn(link1);

        RequestBuilder builder = MockMvcRequestBuilders
                .get("/api/link/{shortenedLink}", link1.getShortenedLink().substring(14));


        mockMvc.perform(builder)
            .andExpect(status().isOk())
            .andDo(print());
    }

    @WithMockUser(username = "admin", password = "password")
    @Test
    void shouldCreateShortenedLink() throws Exception {
        when(linkService.createShortenedLink(any(), any())).thenReturn(link1);

        RequestBuilder builder = MockMvcRequestBuilders
                .post("/api/link/")
                .accept(MediaType.APPLICATION_JSON)
                .content(mapToJson(link1))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.originalLink").value("https://www.youtube.com/watch?3=393dkodwkd3"))
                .andExpect(jsonPath("$.active").value(true))
                .andDo(print());
    }

    @WithMockUser(username = "admin", password = "password")
    @Test
    void shouldExpireShortenedLink() throws Exception {

        when(linkService.expireShortenedLink(any(), any())).thenReturn(true);

        RequestBuilder builder = MockMvcRequestBuilders
                .post("/api/link/{shortenedLink}",  link1.getShortenedLink().substring(14))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Uberlink was expired successfully"))
                .andDo(print());
    }

    @WithMockUser(username = "admin", password = "password")
    @Test
    void shouldGetAllUserLinks() throws Exception {

        link1.setActive(false);
        when(linkService.getAllUserLinksByEmailAddress(any(), any())).thenReturn(new PageImpl<>(List.of(link1, link2)));

        RequestBuilder builder = MockMvcRequestBuilders
                .get("/api/link/all");

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(2))
                .andDo(print());


    }

    @WithMockUser(username = "admin", password = "password")
    @Test
    void shouldGetAllUserActiveLinks() throws Exception {
        link1.setActive(false);

        when(linkService.getAllUserActiveLinksByEmailAddress(any(), any())).thenReturn(new PageImpl<>(List.of(link2)));

        RequestBuilder builder = MockMvcRequestBuilders
                .get("/api/link/all/active");

        mockMvc.perform(builder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(1))
                .andDo(print());


    }

    @WithMockUser(username = "admin", password = "password")
    @Test
    void shouldDeleteByShortenedLink() throws Exception {
        when(linkService.deleteByShortenedLink(link1.getShortenedLink(), link1.getLinkCreatorEmailAddress())).thenReturn(true);
        when(linkService.getAllUserLinksByEmailAddress(any(), any())).thenReturn(new PageImpl<>(List.of(link1)));
        RequestBuilder builder = MockMvcRequestBuilders
                .delete("/api/link/{shortenedLink}", link1.getShortenedLink().substring(14));


        mockMvc.perform(builder)
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$").value("Sorry, this Uberlink does not exist."))
                .andDo(print());
    }

}
