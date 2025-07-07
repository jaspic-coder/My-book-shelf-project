package org.example.mybooklibrary.exception;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

// A global way of handling Springboot Exceptions
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException e, HttpServletRequest req) {
        var response = new ErrorResponse();
        response.setMessage(e.getMessage());
        response.setStatus(HttpStatus.NOT_FOUND.value()); // 404
        response.setPath(req.getRequestURI());
        response.setTimestamp(LocalDateTime.now());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest req) {
        List<String> validationErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        var  response = new ErrorResponse();
        response.setMessage("Validation Failed");
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setPath(req.getDescription(false));
        response.setTimestamp(LocalDateTime.now());
        response.setErrors(validationErrors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//        @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException e, HttpServletRequest req) {
//        List<String> validationErrors = e.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .map(FieldError::getDefaultMessage)
//                .toList();
//
//        var  response = new ErrorResponse();
//        response.setMessage("Validation Failed");
//        response.setStatus(HttpStatus.BAD_REQUEST.value());
//        response.setPath(req.getRequestURI());
//        response.setTimestamp(LocalDateTime.now());
//        response.setErrors(validationErrors);
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//
//    }

    @ExceptionHandler(AuthenticationException.class) // It is thrown when authenticating a user fails
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e, HttpServletRequest req) {
        var response = new ErrorResponse();
        response.setMessage("Authentication Failed");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setPath(req.getRequestURI());
        response.setTimestamp(LocalDateTime.now());

        return  new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AccessDeniedException.class) // Thrown when a user tries to access routes with limited access or wrong roles or permissions
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e, HttpServletRequest req) {
        var response = new ErrorResponse();
        response.setMessage("Access Denied");
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setPath(req.getRequestURI());
        response.setTimestamp(LocalDateTime.now());
        return  new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e, HttpServletRequest req) {
        var response = new ErrorResponse();
        response.setMessage("Internal Server Error");
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setPath(req.getRequestURI());
        response.setTimestamp(LocalDateTime.now());
        return  new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}