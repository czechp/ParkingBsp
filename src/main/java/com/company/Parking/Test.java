package com.company.Parking;

import com.company.Parking.model.Car;
import com.company.Parking.model.Report;
import com.company.Parking.repository.CarRepository;
import com.company.Parking.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Test {
    private CarRepository carRepository;
    private ReportRepository reportRepository;

    @Autowired
    public Test(CarRepository carRepository, ReportRepository reportRepository) {
        this.carRepository = carRepository;
        this.reportRepository = reportRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init(){
        Car car = new Car("1234567","red", "volvo", "s40");
        car.addReport(new Report("xxxx1"));
        car.addReport(new Report("xxxx2"));
        car.addReport(new Report("xxxx3"));
        car.addReport(new Report("xxxx4"));
        carRepository.save(car);
        System.out.println(carRepository.findAll());

        System.out.println(reportRepository.findAll());

        carRepository.deleteById(1L);

        System.out.println(carRepository.findAll());

        System.out.println(reportRepository.findAll());
    }
}
