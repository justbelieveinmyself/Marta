package com.justbelieveinmyself.marta.domain.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
@Data
@AllArgsConstructor
public class AccessToken {
    private Instant expiration;
    private String token;
}
