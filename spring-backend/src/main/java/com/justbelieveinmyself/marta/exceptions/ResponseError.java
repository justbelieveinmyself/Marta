package com.justbelieveinmyself.marta.exceptions;

import lombok.Data;

import java.util.Date;

@Data
public class ResponseError {
    private int status;
    private String message;
    private Date timestamp;

    public ResponseError(int status, String message) {
        this.status = status;
        this.message = message;
        this.timestamp = new Date();
    }
}
