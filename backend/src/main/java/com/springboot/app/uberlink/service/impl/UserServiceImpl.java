package com.springboot.app.uberlink.service.impl;


import com.springboot.app.uberlink.dto.UserAndLinkDTO;
import com.springboot.app.uberlink.dto.UserLoginDTO;
import com.springboot.app.uberlink.exception.exceptions.AuthenticationException;
import com.springboot.app.uberlink.exception.exceptions.UserException;
import com.springboot.app.uberlink.model.ERole;
import com.springboot.app.uberlink.model.Link;
import com.springboot.app.uberlink.model.Role;
import com.springboot.app.uberlink.model.User;
import com.springboot.app.uberlink.payload.request.LoginRequest;
import com.springboot.app.uberlink.payload.request.UpdateAccountDetailsRequest;
import com.springboot.app.uberlink.repository.UserRepository;
import com.springboot.app.uberlink.security.MyUserDetails;
import com.springboot.app.uberlink.security.jwt.JwtTokenProvider;
import com.springboot.app.uberlink.service.RoleService;
import com.springboot.app.uberlink.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final ModelMapper modelMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;


    @Override
    public User findUserById(long id) {
        User user = userRepository.getById(id);
        if (user == null) {
            throw new UsernameNotFoundException("User with id " + id + " does not exist");
        }
        return user;
    }

    @Override
    public User findUserByEmailAddress(String emailAddress) {
        User user = userRepository.findUserByEmailAddress(emailAddress);
        if (user == null) {
            throw new UsernameNotFoundException("User with email address " + emailAddress + " does not exist");
        }
        return user;
    }

    @Override
    public UserLoginDTO login(LoginRequest user) {

        UserLoginDTO userLoginDTO = null;
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getEmailAddress(), user.getPassword()));
            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
            String token = tokenProvider.createToken(authentication);

            userLoginDTO = new UserLoginDTO(userDetails.getFullName(), userDetails.getUsername(), token, userDetails.getAuthorities());
        } catch (DisabledException e) {
            throw new AuthenticationException("User account is disabled");
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Invalid credentials");
        }
        return userLoginDTO;
    }

    @Override
    public User register(User user) {


        Role role = roleService.findByName(ERole.ROLE_USER);

        Set<Role> roleSet = new HashSet<>();
        user.setFullName(user.getFullName());
        user.setEmailAddress(user.getEmailAddress());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword("");
        roleSet.add(role);
        user.setRoles(roleSet);

        user.setEmailVerified(true);

        if (userRepository.existsByEmailAddress(user.getEmailAddress())) {
            throw new UserException("Account already exists with this email");
        }
        return userRepository.save(user);
    }

    @Override
    public void changePassword(String emailAddress, String oldPassword, String newPassword) {
        User user = userRepository.findUserByEmailAddress(emailAddress);

        if (newPassword.length() < 6) {
            throw new AuthenticationException("Must be at least 6 characters");
        }

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new AuthenticationException("Old password is invalid");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }


    @Override
    public void resetPassword(String emailAddress, String newPassword) {
        User user = userRepository.findUserByEmailAddress(emailAddress);

        if (newPassword.length() < 6) {
            throw new AuthenticationException("Must be at least 6 characters");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }


    @Override
    public User updateUser(UpdateAccountDetailsRequest user) {
        User theUser = userRepository.getById(user.getId());

        if(userRepository.existsByEmailAddress(user.getEmailAddress()) && !(user.getEmailAddress().equals(theUser.getEmailAddress()))) {
            throw new UserException("User already exists with this email address.");
        }

        List<Link> links = theUser.getLinks();

        theUser.setFullName(user.getFullName());
        theUser.setEmailAddress(user.getEmailAddress());
        userRepository.save(theUser);


        return theUser;
    }


    @Override
    public UserAndLinkDTO getUserAndLinkInfo(String emailAddress) {

        User user = userRepository.findUserByEmailAddress(emailAddress);

        if (user == null) {
            throw new UsernameNotFoundException("User with email address " + emailAddress + " does not exist");
        }


        return convertEntityToDTO(user);
    }


    private <T> T convertEntityToDTO(User user) {
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE);

        return (T) modelMapper.map(user, UserAndLinkDTO.class);
    }


}
