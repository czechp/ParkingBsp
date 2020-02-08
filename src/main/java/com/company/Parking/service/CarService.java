package com.company.Parking.service;

import com.company.Parking.model.Car;
import com.company.Parking.model.Report;
import com.company.Parking.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.time.LocalDateTime;
import java.util.*;

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

    public List<Car> findAllCar(){
        try {
            List<Car> result = carRepository.findAll();
            sortCarReportsByDate(result);
            return result;

        } catch (Exception e) {
            log.error("Error during getting all cars", e);
        }
        return new ArrayList<>();
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

    private void sortCarReportsByDate(List<Car> cars){
        for (Car car : cars) {
            List<Report> reportList =  new ArrayList<>(car.getReports());
            Collections.sort(reportList);
            car.setReports(new LinkedHashSet<>(reportList));
        }
    }
}
