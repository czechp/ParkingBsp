package com.company.Parking.controller;

import com.company.Parking.model.Car;
import com.company.Parking.model.Report;
import com.company.Parking.repository.ReportRepository;
import com.company.Parking.service.CarRepoService;
import com.company.Parking.service.ConvertionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
@Slf4j
public class CarController implements ErrorController {

    CarRepoService carRepoService;

    @Autowired
    public CarController(CarRepoService carRepoService, ReportRepository reportRepository) {
        this.carRepoService = carRepoService;
    }

    @GetMapping
    public ModelAndView main() {
        ModelAndView modelAndView = new ModelAndView("index");

        modelAndView.addObject("cars", carRepoService.findAllCar());
        return modelAndView;
    }

    @GetMapping("/add")
    public ModelAndView addNewCar() {
        ModelAndView modelAndView = new ModelAndView("Add/add");
        modelAndView.addObject("car", new Car());
        return modelAndView;
    }


    //ADD PRINCIPAL
    @PostMapping("/added")
    public String addedNewCar(@ModelAttribute @Valid Car car, Errors errors) {
        if (errors.hasErrors())
            return "Add/add_failure_data_incorrect";
        else if (!carRepoService.createCar(car, new Report("Unknown")))
            return "Add/entry_already_added";
        return "redirect:/";
    }

    @GetMapping("/car_delete")
    public ModelAndView carDelete() {
        ModelAndView modelAndView = new ModelAndView("Delete/get_car_to_delete");
        List<@Pattern(regexp = "^[A-Z,0-9]{7}$") String> regs = carRepoService.findAllCar().stream()
                .map(Car::getRegTable)
                .collect(Collectors.toList());
        modelAndView.addObject("regs", regs);
        return modelAndView;
    }

    //ADD PRINCIPAL
    @GetMapping("/car_delete_all")
    public String carDeleteAll(@RequestParam(name = "reg_table") String regTable) {
        if (carRepoService.deleteCarByRegTable(regTable))
            return "Delete/car_deleted";
        return "Delete/car_deleted_failed";
    }

    //ADD PRINCIPAL
    @GetMapping("/car_delete_details")
    public ModelAndView carDeleteDetails(@RequestParam(name = "reg_table") String reg) {
        Optional<Car> carByRegTable = carRepoService.getCarByRegTable(reg);
        ModelAndView modelAndView = new ModelAndView("Delete/car_deleted_failed");
        if (carByRegTable.isPresent()) {
            modelAndView.addObject("carDetails", carByRegTable.get());
            modelAndView.setViewName("Delete/get_car_to_delete_details");
            return modelAndView;
        }
        return modelAndView;
    }

    //ADD PRINCIPAL
    @GetMapping("/car_delete_details_deleted")
    public String carDeleteDetailDeleted(@RequestParam(name = "reportDate") String reportDate, @RequestParam(name = "regTable") String regTable) {
        if (carRepoService.deleteReportFromCar(regTable, ConvertionService.stringToLocalDate(reportDate)))
            return "Delete/car_deleted";
        return "Delete/car_deleted_failed";
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        int statusCode = Integer.parseInt(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE).toString());
        log.error("Error -------------- " + HttpStatus.valueOf(statusCode));

        if (statusCode == HttpStatus.NOT_FOUND.value())
            return "Errors/error_not_found";

        return "Errors/error";
    }


    @Override
    public String getErrorPath() {
        return "/error";
    }
}
