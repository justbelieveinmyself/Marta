package com.justbelieveinmyself.marta.exceptions.advicer;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.justbelieveinmyself.marta.exceptions.*;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(value = {NotFoundException.class, MissingPathVariableException.class})
    public ResponseEntity<Object> handleNotFoundException(Exception e){
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

    @ExceptionHandler(value = RefreshTokenException.class)
    public ResponseEntity<ResponseError> handleRefreshTokenException(RefreshTokenException e){
        ResponseError responseError = new ResponseError(HttpStatus.UNAUTHORIZED, e.getMessage());
        return new ResponseEntity<>(responseError, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = FileSizeLimitExceededException.class)
    public ResponseEntity<ResponseError> handleFileSizeLimitExceededException(FileSizeLimitExceededException e){
        ResponseError responseError = new ResponseError(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        return new ResponseEntity<>(responseError, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(value = TokenExpiredException.class)
    public ResponseEntity<ResponseError> handleTokenExpiredException(TokenExpiredException e){
        ResponseError responseError = new ResponseError(HttpStatus.UNAUTHORIZED, e.getMessage());
        return new ResponseEntity<>(responseError, HttpStatus.UNAUTHORIZED);
    }
}
