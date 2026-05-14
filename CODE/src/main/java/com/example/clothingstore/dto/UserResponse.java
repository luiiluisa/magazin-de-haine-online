package com.example.clothingstore.dto;

import com.example.clothingstore.model.Role;
import com.example.clothingstore.model.User;

public class UserResponse {
    private Long id;
    private String fullName;
    private String username;
    private Role role;

    public UserResponse(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.username = user.getUsername();
        this.role = user.getRole();
    }

    public Long getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public Role getRole() {
        return role;
    }
}
