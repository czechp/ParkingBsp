package com.company.Parking;

import com.company.Parking.configuration.PasswordEncoderConfiguration;
import com.company.Parking.model.AppUser;
import com.company.Parking.model.Car;
import com.company.Parking.model.Report;
import com.company.Parking.repository.AppUserRepository;
import com.company.Parking.repository.CarRepository;
import com.company.Parking.repository.ReportRepository;
import com.company.Parking.service.CarRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Profile(value = "development")
@Component
public class Test {
    private CarRepository carRepository;
    private ReportRepository reportRepository;
    private CarRepoService carService;
    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public Test(CarRepository carRepository, ReportRepository reportRepository, CarRepoService carService, AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.carRepository = carRepository;
        this.reportRepository = reportRepository;
        this.carService = carService;
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        Car car = new Car("1234567", "red", "volvo", "s40");
        Car car1 = new Car("5678907", "red", "volvo", "s40");
        Report report = new Report("Przemek");
        Report report1 = new Report("Maniek");
        car.addReport(report);
        car.addReport(report1);

        car1.addReport(new Report("Przemek"));
        car1.addReport(new Report("Maniek"));

        carRepository.save(car);
        carRepository.save(car1);

        AppUser user = new AppUser("admin", passwordEncoder.encode("admin"), "webcoderc@gmail.com");
        user.setEmailVerification(true);
        user.setAdminVerification(true);
        appUserRepository.save(user);
    }
}
