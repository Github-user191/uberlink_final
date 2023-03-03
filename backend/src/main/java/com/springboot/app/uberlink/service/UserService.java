package com.springboot.app.uberlink.service;


import com.springboot.app.uberlink.dto.UserAndLinkDTO;
import com.springboot.app.uberlink.dto.UserLoginDTO;
import com.springboot.app.uberlink.model.User;
import com.springboot.app.uberlink.payload.request.LoginRequest;
import com.springboot.app.uberlink.payload.request.UpdateAccountDetailsRequest;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    User findUserById(long id);
    User findUserByEmailAddress(String emailAddress);
    UserLoginDTO login(LoginRequest user);
    User register(User user);
    User updateUser(UpdateAccountDetailsRequest user);
    UserAndLinkDTO getUserAndLinkInfo(String emailAddress);

    void changePassword(String emailAddress, String oldPassword, String newPassword);
    void resetPassword(String emailAddress, String newPassword);
    boolean checkIfValidOldPassword(User user, String oldPassword);
}
