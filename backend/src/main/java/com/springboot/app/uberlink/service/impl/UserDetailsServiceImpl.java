package com.springboot.app.uberlink.service.impl;

import com.springboot.app.uberlink.exception.exceptions.AuthenticationException;
import com.springboot.app.uberlink.model.User;
import com.springboot.app.uberlink.repository.UserRepository;
import com.springboot.app.uberlink.security.MyUserDetails;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String emailAddress) throws UsernameNotFoundException {

        User user = userRepository.findUserByEmailAddress(emailAddress);


        if(user == null) {
            throw new AuthenticationException("Invalid credentials");
        }
        if(!user.isEmailVerified()) {
            throw new AuthenticationException("Your account is not verified. Please check your email for a confirmation link");
        }

        return MyUserDetails.createUser(user);
    }

}
