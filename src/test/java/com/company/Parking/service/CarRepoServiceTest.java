package com.company.Parking.service;

import com.company.Parking.model.Car;
import com.company.Parking.model.Report;
import com.company.Parking.repository.CarRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CarRepoServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarRepoService carService;

    @Test
    public void createCar_CarNotExitTest() {
        //given
        Car car = new Car("RLE1234", "red", "volvo", "s40");
        Report report = new Report("Andrzej");
        //when
        when(carRepository.save(car)).thenReturn(car);
        when(carRepository.findAllByRegTable(car.getRegTable())).thenReturn(Optional.of(car));
        boolean result = carService.createCar(car, report);
        //then
        verify(carRepository, times(3)).findAllByRegTable(car.getRegTable());
        verify(carRepository, times(1)).save(car);
        assertTrue(result);
    }

    @Test
    public void createCar_CarExistTest() {
        //given
        Car car = new Car("RLE1234", "red", "volvo", "s40");
        Report report = new Report("Andrzej");
        //when
        when(carRepository.findAllByRegTable(car.getRegTable())).thenReturn(Optional.empty());
        when(carRepository.save(car)).thenReturn(car);
        boolean result = carService.createCar(car, report);
        //then
        verify(carRepository, times(2)).findAllByRegTable(car.getRegTable());
        verify(carRepository, times(1)).save(car);
        assertTrue(result);
    }

    @Test
    public void createCarTest_CarHasEntryTodayAlready() {
        //given
        Car car = new Car("RLE1234", "red", "volvo", "s40");
        Report report = new Report("Andrzej");
        car.addReport(report);
        //when
        when(carRepository.findAllByRegTable(car.getRegTable())).thenReturn(Optional.of(car));
        boolean result = carService.createCar(car, report);
        //then
        assertFalse(result);
    }

    @Test
    public void findAllCarsTest() {
        //given
        List<Car> cars = Arrays.asList(
                new Car(),
                new Car(),
                new Car()
        );
        //when
        when(carRepository.findAll()).thenReturn(cars);
        List<Car> result = carService.findAllCar();
        //then
        assertThat(result, hasSize(cars.size()));
    }

    @Test
    public void deleteCarByRegTableTest_CarExist() {
        //given
        Car car = new Car();
        String regTable = "1234567";
        car.setRegTable(regTable);
        //when
        when(carRepository.findAllByRegTable(regTable)).thenReturn(Optional.of(car));
        doNothing().when(carRepository).delete(car);
        boolean result = carService.deleteCarByRegTable(regTable);
        //then
        assertTrue(result);
    }

    @Test
    public void deleteCarByRegTableTest_CarNotExist() {
        //given
        String regTable = "1234567";
        //when
        when(carRepository.findAllByRegTable(regTable)).thenReturn(Optional.empty());
        boolean result = carService.deleteCarByRegTable(regTable);
        //then
        assertFalse(result);
    }

    @Test
    public void getCarByRegTableTest_CarExist() {
        //given
        Car car = new Car("1234567", "xxxxx", "aaaaaa", "asdas");
        String regTable = "1234567";
        //when
        when(carRepository.findAllByRegTable(regTable)).thenReturn(Optional.of(car));
        Optional<Car> result = carService.getCarByRegTable(regTable);
        //then
        assertTrue(result.isPresent());
        assertEquals(car, result.get());
    }

    @Test
    public void getCarByRegTableTest_CarNotExist(){
        //given
        String regTable  = "1234567";
        //when
        when(carRepository.findAllByRegTable(regTable)).thenReturn(Optional.empty());
        Optional<Car> result = carService.getCarByRegTable(regTable);
        //then
        assertFalse(result.isPresent());
    }

    @Test
    public void deleteReportFromCarTest(){
        //given
        String regTable = "1234567";
        Car car = new Car(regTable, "qqqq", "wwwww", "eeeee");
        car.addReport(new Report("xxxxxxxx"));
        car.addReport(new Report("ssssssss"));
        //when
        when(carRepository.findAllByRegTable(regTable)).thenReturn(Optional.of(car));
        when(carRepository.save(car)).thenReturn(car);
        boolean result = carService.deleteReportFromCar(regTable, LocalDate.now());
        //then
        assertTrue(result);
        verify(carRepository, times(1)).save(car);
    }

    @Test
    public void deleteReportFromCarTest_CarNotExists(){
        //given
        String regTable = "1234567";
        Car car = new Car(regTable, "qqqq", "wwwww", "eeeee");
        car.addReport(new Report("xxxxxxxx"));
        //when
        when(carRepository.findAllByRegTable(regTable)).thenReturn(Optional.empty());
        boolean result = carService.deleteReportFromCar(regTable, LocalDate.now());
        //then
        assertFalse(result);
    }

    @Test
    public void deleteReportFromCarTest_ReportNotExists(){
        //given
        String regTable = "1234567";
        Car car = new Car(regTable, "qqqq", "wwwww", "eeeee");
        //when
        when(carRepository.findAllByRegTable(regTable)).thenReturn(Optional.of(car));
        boolean result = carService.deleteReportFromCar(regTable, LocalDate.now());
        //then
        assertFalse(result);
    }

    @Test
    public void deleteReportFromCarTest_AfterDeleteCarIsEmpty(){
        //given
        String regTable = "1234567";
        Car car = new Car(regTable, "qqqq", "wwwww", "eeeee");
        car.addReport(new Report("xxxxxxxx"));
        //when
        when(carRepository.findAllByRegTable(regTable)).thenReturn(Optional.of(car));
        doNothing().when(carRepository).delete(car);
        boolean result = carService.deleteReportFromCar(regTable, LocalDate.now());
        //then
        assertTrue(result);
        verify(carRepository, times(1)).delete(car);

    }

}