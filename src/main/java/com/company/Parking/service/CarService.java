package com.company.Parking.service;

import com.company.Parking.model.Car;
import com.company.Parking.model.Report;
import com.company.Parking.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class CarService {

    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public boolean createCar(Car car, Report report) {
        if (!isCarHasEntryToday(car)) {
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

    public List<Car> findAllCar() {
        try {
            List<Car> result = carRepository.findAll();
            sortCarReportsByDate(result);
            return result;

        } catch (Exception e) {
            log.error("Error during getting all cars", e);
        }
        return new ArrayList<>();
    }

    public boolean deleteCarByRegTable(String regTable) {
        Optional<Car> carByRegTable = getCarByRegTable(regTable);
        if (carByRegTable.isPresent()) {
            deleteCar(carByRegTable.get());
            return true;
        }
        return false;
    }


    private boolean deleteCar(Car car) {
        try {
            carRepository.delete(car);
            return true;
        } catch (Exception e) {
            log.error("Error during removing car", e);
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

    private void sortCarReportsByDate(List<Car> cars) {
        for (Car car : cars) {
            List<Report> reportList = new ArrayList<>(car.getReports());
            Collections.sort(reportList);
            car.setReports(new LinkedHashSet<>(reportList));
        }
    }

    private boolean isCarHasEntryToday(Car car) {
        Optional<Car> result = getCarByRegTable(car.getRegTable());
        if (result.isPresent()) {
            for (Report report : result.get().getReports()) {
                if (report.getDate().equals(LocalDate.now()))
                    return true;
            }
        }
        return false;
    }
}
