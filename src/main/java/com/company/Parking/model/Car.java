package com.company.Parking.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "cars")
@Data
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Pattern(regexp = "^[A-Z,0-9]{7}$")
    private String regTable;

    @NotNull
    private String color;


    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Report> reports = new HashSet<>();

    @NotNull
    @NotEmpty
    private String mark;

    @NotNull
    @NotEmpty
    private String model;


    private int amountPark;

    public Car() {

    }

    public Car(@NotBlank @NotNull @Pattern(regexp = "^[A-Z,0-9]{7}$") String regTable, @NotNull String color, @NotNull @NotEmpty String mark, @NotNull @NotEmpty String model) {
        this.regTable = regTable;
        this.color = color;
        this.mark = mark;
        this.model = model;
    }



    public void addReport(Report report){
        reports.add(report);
        amountPark = reports.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return regTable.equals(car.regTable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(regTable);
    }
}
