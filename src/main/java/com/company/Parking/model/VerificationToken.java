package com.company.Parking.model;

import lombok.Data;

import javax.persistence.*;
import java.rmi.server.UID;
import java.util.UUID;


@Entity
@Table(name = "tokens")
@Data
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String token;

    @OneToOne
    private AppUser appUser;

    public VerificationToken() {
    }

    public VerificationToken(AppUser appUser) {
        this.token = UUID.randomUUID().toString();
        this.appUser = appUser;
    }
}
