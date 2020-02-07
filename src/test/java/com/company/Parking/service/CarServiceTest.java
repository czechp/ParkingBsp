package com.company.Parking.service;

import com.company.Parking.model.Car;
import com.company.Parking.model.Report;
import com.company.Parking.repository.CarRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.Errors;

import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    @Test
    public void createCar_CarNotExitTest() {
        //given
        Errors errors = mock(Errors.class);
        Car car = new Car("RLE1234", "red", "volvo", "s40");
        Report report = new Report("Andrzej");
        //when
        when(errors.hasErrors()).thenReturn(false);
        when(carRepository.save(car)).thenReturn(car);
        when(carRepository.findAllByRegTable(car.getRegTable())).thenReturn(Optional.of(car));
        boolean result = carService.createCar(car, report, errors);
        //then
        verify(carRepository, times(2)).findAllByRegTable(car.getRegTable());
        verify(carRepository, times(1)).save(car);
        assertTrue(result);
    }

    @Test
    public void createCar_CarExistTest() {
        //given
        Errors errors = mock(Errors.class);
        Car car = new Car("RLE1234", "red", "volvo", "s40");
        Report report = new Report("Andrzej");
        //when
        when(errors.hasErrors()).thenReturn(false);
        when(carRepository.findAllByRegTable(car.getRegTable())).thenReturn(Optional.empty());
        when(carRepository.save(car)).thenReturn(car);
        boolean result = carService.createCar(car, report, errors);
        //then
        verify(carRepository, times(1)).findAllByRegTable(car.getRegTable());
        verify(carRepository, times(1)).save(car);
        assertTrue(result);
    }

    @Test
    public void createCar_HasErrorsTest(){
        //given
        Car car = new Car("RLE1234", "red", "volvo", "s40");
        Report report = new Report("Andrzej");
        Errors errors = mock(Errors.class);
        //when
        when(errors.hasErrors()).thenReturn(true);
        boolean result = carService.createCar(car, report, errors);
        //then
        assertFalse(result);
    }

}