package com.company.Parking.repository;

import com.company.Parking.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Optional<Car> findByRegTable(String regTable);
    List<Car> findAllById(Long id);
    List<Car> findAllByRegTable(String regTable);
    List<Car> findAllByColor(String color);

    boolean existsCarByRegTable(String regTable);
}
