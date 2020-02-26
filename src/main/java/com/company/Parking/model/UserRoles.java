package com.company.Parking.model;

public enum UserRoles {
    ADMIN("ADMIN"),
    USER("USER");

    private String role;

    UserRoles(String role) {
        this.role = role;
    }

    public String getRoleName(){
        return this.role;
    }
}
