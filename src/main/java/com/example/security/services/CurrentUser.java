package com.example.security.services;

import com.example.model.constraints.Role;
import lombok.Getter;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class CurrentUser extends User {

    private final Long userId;
    private final Role role;

    public CurrentUser(String username, String password, boolean enabled, Long userId, Role role) {
        super(username, password,
                enabled, true,
                true, true,
                Collections.singletonList(role));

        this.userId = userId;
        this.role = role;
    }

    @Override
    public String toString() {
        return getClass().getName() + " [" +
                "Email=" + getUsername() + ", " +
                "ID=" + getUserId() + ", " +
                "Enabled=" + isEnabled() + ", " +
                "Role=" + getRole() + "]";
    }
}