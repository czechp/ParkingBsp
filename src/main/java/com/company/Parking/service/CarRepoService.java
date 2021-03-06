package com.company.Parking.service;

import com.company.Parking.model.Car;
import com.company.Parking.model.Report;
import com.company.Parking.repository.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CarRepoService {

    private CarRepository carRepository;

    @Autowired
    public CarRepoService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<String> getRegsList() {
        return findAllCar().stream()
                .map(Car::getRegTable)
                .collect(Collectors.toList());
    }

    public boolean createCar(Car car, Report report) {
        if (!isCarHasEntryToday(car)) {
            if (existsCar(car)) {
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

    public boolean deleteReportFromCar(String regTable, LocalDate reportDate) {
        Optional<Car> optCar = getCarByRegTable(regTable);
        if (optCar.isPresent()) {
            Car car = optCar.get();
            Optional<Report> report = car.getReports().stream()
                    .filter(x -> x.getDate().equals(reportDate))
                    .findFirst();
            if (report.isPresent()) {
                car.getReports().remove(report.get());
                if (car.getReports().isEmpty())
                    deleteCar(car);
                else
                    saveNew(car);
                return true;
            }
        }
        return false;
    }

    public boolean saveModifiedCar(@Valid Car car) {
        try {
            Car result = carRepository.save(car);
            return result.equals(car);
        } catch (Exception e) {
            log.error("Error during saving modified car", e);
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

    private boolean existsCar(Car car) {
        Optional<Car> result = Optional.empty();
        try {
            result = carRepository.findByRegTable(car.getRegTable());
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

    public Optional<Car> getCarByRegTable(String regTable) {
        try {
            return carRepository.findByRegTable(regTable);
        } catch (Exception e) {
            log.error("Error during getting car by register table", e);
        }
        return Optional.empty();
    }

    public List<Car> searchCarByField(String field, String content) {
        switch (field) {
            case "Id":
                return searchCarsById(content);
            case "Numer rejestracji":
                return searchCarsByRegTable(content);
            case "Kolor":
                return searchCarByColor(content);
            case "Marka":
                return searchCarByMark(content);
            case "Model":
                return searchCarByModel(content);
            default:
                return Arrays.asList();
        }
    }

    private List<Car> searchCarByMark(String mark) {
        try {
             return carRepository.findAllByMark(mark);
        } catch (Exception e) {
            log.error("Error during finding cars by mark", e);
        }
        return new ArrayList<>();
    }

    private List<Car> searchCarByModel(String model) {
        try {
            return carRepository.findAllByModel(model);
        } catch (Exception e) {
            log.error("Error during finding cars by model", e);
        }
        return new ArrayList<>();
    }

    private List<Car> searchCarByColor(String color) {
        try {
            return carRepository.findAllByColor(color.replaceAll(" ",""));
        } catch (Exception e) {
            log.error("Error during finding cars by color", e);
        }
        return new ArrayList<>();
    }

    private List<Car> searchCarsByRegTable(String regTable) {
        try {
             return carRepository.findAllByRegTable(regTable.replaceAll(" ", ""));
        } catch (Exception e) {
            log.error("Error during finding cars by Register table", e);
        }
        return new ArrayList<>();
    }

    private List<Car> searchCarsById(String id) {
        try {
            Long idL = Long.valueOf(id);
            return carRepository.findAllById(idL);
        } catch (NumberFormatException e) {
            log.error("Cannot parse ID -> Long or Error during finding cars by ID", e);
        }

        return new ArrayList<>();
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
