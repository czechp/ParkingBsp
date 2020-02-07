package com.company.Parking.service;

import com.company.Parking.model.Car;
import com.company.Parking.model.Report;
import com.company.Parking.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Optional;

@Service
@Slf4j
public class CarService {

    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public boolean createCar(Car car, Report report, Errors errors) {
        if (!errors.hasErrors()) {
            if (existByRegTable(car)) {
                Car existCar = getCarByRegTable(car.getRegTable()).get();
                existCar.addReport(report);
                saveNew(existCar);
            } else {
                car.addReport(report);
                saveNew(car);
            }
            return true;
        }
        return false;
    }

    private boolean existByRegTable(Car car) {
        Optional<Car> result = Optional.empty();
        try {
            result = carRepository.findAllByRegTable(car.getRegTable());
        } catch (Exception e) {
            log.error("Error during searching car by car register table", e);
        }
        return result.isPresent();
    }

    private void saveNew(Car car) {
        try {
            carRepository.save(car);
        } catch (Exception e) {
            log.error("Error during saving new car");
        }
    }

    private Optional<Car> getCarByRegTable(String regTable) {
        try {
            return carRepository.findAllByRegTable(regTable);
        } catch (Exception e) {
            log.error("Error during getting car by register table", e);
        }
        return Optional.empty();
    }
}
