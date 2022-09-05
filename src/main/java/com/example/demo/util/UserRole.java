package com.example.demo.util;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    ROLE_ADMIN, ROLE_USER, ROLE_GUEST;

    @Override
    public String getAuthority() {
        return name();
    }
}
