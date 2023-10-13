package com.justbelieveinmyself.marta.exceptions.advicer;

import com.justbelieveinmyself.marta.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<Object> handleNotFoundException(NotFoundException e){
        ResponseError responseError = new ResponseError(HttpStatus.NOT_FOUND, e.getMessage());
        return new ResponseEntity<>(responseError, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(value = BadCredentialsException.class)
    public ResponseEntity<ResponseError> handleUnauthorizedExceptions(BadCredentialsException e){
        ResponseError responseError = new ResponseError(HttpStatus.UNAUTHORIZED, e.getMessage());
        return new ResponseEntity<>(responseError, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(value = NotCreatedException.class)
    public ResponseEntity<ResponseError> handleNotCreatedException(NotCreatedException e){
        ResponseError responseError = new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        return new ResponseEntity<>(responseError, HttpStatus.UNPROCESSABLE_ENTITY);
    }
    @ExceptionHandler(value = ForbiddenException.class)
    public ResponseEntity<ResponseError> handleForbiddenException(ForbiddenException e){
        ResponseError responseError = new ResponseError(HttpStatus.FORBIDDEN, e.getMessage());
        return new ResponseEntity<>(responseError, HttpStatus.FORBIDDEN);
    }

}