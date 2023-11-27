package com.justbelieveinmyself.marta.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class ResponseError {
    private HttpStatus status;
    private String message;
    @Builder.Default
    private Date timestamp = new Date();

    public ResponseError(HttpStatus httpStatus, String message) {
        this.status = httpStatus;
        this.message = message;
        this.timestamp = new Date();
    }
}
