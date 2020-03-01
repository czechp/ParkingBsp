package com.company.Parking.model;

import lombok.Data;

import javax.persistence.*;
import java.rmi.server.UID;
import java.util.UUID;


@Entity
@Table(name = "tokens")
@Data
public class VerifiactionToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String token;

    @OneToOne
    private Car car;

    public VerifiactionToken() {
    }

    public VerifiactionToken(Car car) {
        this.token = UUID.randomUUID().toString();
        this.car = car;
    }
}
