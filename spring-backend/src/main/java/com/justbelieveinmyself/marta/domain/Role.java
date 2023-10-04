package com.justbelieveinmyself.marta.domain;

import org.springframework.security.core.GrantedAuthority;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum Role implements GrantedAuthority {
    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }

}
