package com.company.Parking.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "reports")
@Data
public class Report implements Comparable<Report> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @NotEmpty
    private String reporter;

    @NotNull
    private LocalDate date;

    public Report(@NotNull @NotEmpty String reporter) {
        this.reporter = reporter;
        this.date = LocalDate.now();
    }

    public Report() {
    }

    @Override
    public int compareTo(Report o) {
        return this.date.compareTo(o.getDate());
    }
}
