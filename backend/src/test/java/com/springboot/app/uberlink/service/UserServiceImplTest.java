package com.springboot.app.uberlink.service;

import com.springboot.app.uberlink.dto.UserAndLinkDTO;
import com.springboot.app.uberlink.exception.exceptions.AuthenticationException;
import com.springboot.app.uberlink.exception.exceptions.UserException;
import com.springboot.app.uberlink.model.User;
import com.springboot.app.uberlink.repository.UserRepository;
import com.springboot.app.uberlink.service.impl.RoleServiceImpl;
import com.springboot.app.uberlink.service.impl.UserDetailsServiceImpl;
import com.springboot.app.uberlink.service.impl.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleServiceImpl roleService;


    @Mock
    private UserDetailsServiceImpl userDetailsService;


    @Mock
    private PasswordEncoder passwordEncoder;


    @Mock
    private ModelMapper modelMapper;

    private User user;

    @BeforeEach
    void setUp() {

        this.passwordEncoder = new BCryptPasswordEncoder();

        userService = new UserServiceImpl(userRepository, passwordEncoder, roleService, modelMapper,
                null, null);


        user = new User(1L, "John Doe", "johndoe@gmail.com", passwordEncoder.encode("password"));


    }

    @AfterEach
    void tearDown() {

    }


    @Test
    void testIfUserExistsByUserId() {
        when(userRepository.getById(anyLong())).thenReturn(user);

        User user = userService.findUserById(anyLong());

        assertNotNull(user);
        verify(userRepository).getById(anyLong());

    }

    @Test
    void testIfUserDoesNotExistByUserId() {

        long id = 1;
        when(userRepository.getById(anyLong())).thenReturn(null);

        assertThatThrownBy(() -> userService.findUserById(id))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User with id " + id + " does not exist");

        verify(userRepository, times(1)).getById(id);
    }

    @Test
    void testIfUserExistsByEmailAddress() {
        String emailAddress = "johndoe@gmail.com";
        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);


        User theUser = userService.findUserByEmailAddress(emailAddress);
        assertEquals(1, theUser.getId());
        assertEquals(emailAddress, theUser.getEmailAddress());
        assertEquals(user.getPassword(), theUser.getPassword());


    }

    @Test
    void testIfUserDoesNotExistByEmailAddress() {
        String emailAddress = "fakeuser@gmail.com";

        assertThatThrownBy(() -> userService.findUserByEmailAddress(emailAddress))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessageContaining("User with email address " + emailAddress + " does not exist");


        verify(userRepository).findUserByEmailAddress(emailAddress);

    }

    @Test
    void testIfUserCanRegisterSuccessfully() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User created = userService.register(user);

        assertNotNull(created);
        assertEquals("johndoe@gmail.com", created.getEmailAddress());
        verify(userRepository).save(any());
    }

    @Test
    void testIfUserEmailAddressAlreadyTakenWhenRegistering() {
        given(userRepository.existsByEmailAddress(user.getEmailAddress())).willReturn(true);

        assertThatThrownBy(() -> userService.register(user))
                .isInstanceOf(UserException.class)
                .hasMessageContaining("Account already exists with this email");

        // verify method userRepository.save() was never called, because execution terminated
        verify(userRepository, never()).save(any());
    }

    @Test
    void testIfUserCanChangePasswordSuccessfully() {
        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);

        User theUser = userService.findUserByEmailAddress("admin@gmail.com");

        // email, old password, new password
        userService.changePassword("admin@gmail.com", "password", "password123");
        assertTrue(passwordEncoder.matches("password123", theUser.getPassword()));
        verify(userRepository).save(any());
    }


    @Test
    void testIfUserOldPasswordDoesNotMatch(){
        when(userRepository.findUserByEmailAddress(anyString())).thenReturn(user);

        // The old password is "password", here i'm setting it as "fakepassword" to test the exception it throws
        assertThatThrownBy(() -> userService.changePassword("admin@gmail.com", "fakepassword", "password123"))
                .isInstanceOf(AuthenticationException.class)
                .hasMessageContaining("Old password is invalid");

        verify(userRepository, never()).save(any());
    }


    @Test
    @Disabled
    public void testCanGetUserAndLinkInformation() {

        when(userRepository.findUserByEmailAddress("admin@gmail.com")).thenReturn(user);

        UserAndLinkDTO userAndLinkDTO = userService.getUserAndLinkInfo("admin@gmail.com");



    }



}