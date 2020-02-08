package com.company.Parking.model;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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
    private LocalDateTime date;

    public Report(@NotNull @NotEmpty String reporter) {
        this.reporter = reporter;
        this.date = LocalDateTime.now();
    }

    public Report() {
    }

    @Override
    public int compareTo(Report o) {
        return this.date.compareTo(o.getDate());
    }
}
