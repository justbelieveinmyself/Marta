package com.justbelieveinmyself.marta.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {
    private HttpStatus status;
    private String message;
    private Date timestamp;

    public ResponseError(HttpStatus httpStatus, String message) {
        this.status = httpStatus;
        this.message = message;
        this.timestamp = new Date();
    }
}
