package com.springboot.app.uberlink.controller;

import com.springboot.app.uberlink.dto.UserAndLinkDTO;
import com.springboot.app.uberlink.payload.request.UpdateAccountDetailsRequest;
import com.springboot.app.uberlink.service.UserService;
import com.springboot.app.uberlink.service.impl.ErrorValidationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    private final ErrorValidationServiceImpl errorValidationService;


    public UserController(UserService userService, ErrorValidationServiceImpl errorValidationService) {
        this.userService = userService;
        this.errorValidationService = errorValidationService;
    }

    @GetMapping("/info")
    public ResponseEntity<UserAndLinkDTO> getUserAndPostInfo(Principal principal) {
        UserAndLinkDTO theUser = userService.getUserAndLinkInfo(principal.getName());

        return ResponseEntity.ok(theUser);
    }


    @PutMapping("/update")
    public ResponseEntity<?> updateUserDetails(@Valid @RequestBody UpdateAccountDetailsRequest user, BindingResult result, Principal principal) {
        ResponseEntity<?> errorMap = errorValidationService.validationService(result);
        if(errorMap != null) return errorMap;


        userService.updateUser(user);

        return new ResponseEntity<>(user, HttpStatus.OK);

    }


}
