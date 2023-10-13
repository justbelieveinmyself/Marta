package com.justbelieveinmyself.marta.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
@Data
public class NotCreatedException extends RuntimeException{
    public NotCreatedException(String message) {
        super(message);
    }
}
