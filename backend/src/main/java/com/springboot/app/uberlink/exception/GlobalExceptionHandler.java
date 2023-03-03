package com.springboot.app.uberlink.exception;

import com.springboot.app.uberlink.exception.exceptions.AuthenticationException;
import com.springboot.app.uberlink.exception.exceptions.LinkException;
import com.springboot.app.uberlink.exception.exceptions.TokenException;
import com.springboot.app.uberlink.exception.exceptions.UserException;
import com.springboot.app.uberlink.exception.response.AuthenticationExceptionResponse;
import com.springboot.app.uberlink.exception.response.LinkExceptionResponse;
import com.springboot.app.uberlink.exception.response.TokenExceptionResponse;
import com.springboot.app.uberlink.exception.response.UserExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<?> handleLinkException(LinkException ex, WebRequest req) {
        LinkExceptionResponse exceptionResponse = new LinkExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handleAuthenticationException(AuthenticationException ex, WebRequest req) {
        AuthenticationExceptionResponse exceptionResponse = new AuthenticationExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handleUserException(UserException ex, WebRequest req) {
        UserExceptionResponse exceptionResponse = new UserExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<?> handleTokenException(TokenException ex, WebRequest req) {
        TokenExceptionResponse exceptionResponse = new TokenExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
