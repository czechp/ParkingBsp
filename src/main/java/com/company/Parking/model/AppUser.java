package com.company.Parking.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
public class AppUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

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


    public AppUser() {
        this.role = UserRoles.USER.getRoleName();
    }

    public AppUser(@NotNull @NotBlank String username, @NotNull @NotBlank String password, @Email @NotNull String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = UserRoles.USER.getRoleName();
    }

    public void setAdminRole() {
        this.role = UserRoles.ADMIN.getRoleName();
    }

    public void setUserRole() {
        this.role = UserRoles.USER.getRoleName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_".concat(role)));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return emailVerification && adminVerification;
    }
}
