package com.company.Parking.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class AppUser {

    @NotNull
    @NotBlank
    private String username;

    @NotNull
    @NotBlank
    private String password;

    @Email
    @NotNull
    private String email;

    private String role;

    private boolean emailVerification;

    private boolean adminVerification;


    private boolean enable;

    public AppUser() {
    }

    public AppUser(@NotNull @NotBlank String username, @NotNull @NotBlank String password, @Email @NotNull String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
