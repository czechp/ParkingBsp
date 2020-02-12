package com.company.Parking.controller;

import com.company.Parking.model.Car;
import com.company.Parking.model.Report;
import com.company.Parking.repository.ReportRepository;
import com.company.Parking.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class CarController {

    CarService carService;

    @Autowired
    public CarController(CarService carService, ReportRepository reportRepository) {
        this.carService = carService;
    }

    @GetMapping
    public ModelAndView main() {
        ModelAndView modelAndView = new ModelAndView("index");

        modelAndView.addObject("cars", carService.findAllCar());
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
        else if (!carService.createCar(car, new Report("Unknown")))
            return "Add/entry_already_added";
        return "redirect:/";
    }

    @GetMapping("/car_delete")
    public ModelAndView carDelete() {
        ModelAndView modelAndView = new ModelAndView("Delete/get_car_to_delete");
        List<@Pattern(regexp = "^[A-Z,0-9]{7}$") String> regs = carService.findAllCar().stream()
                .map(Car::getRegTable)
                .collect(Collectors.toList());
        modelAndView.addObject("regs", regs);
        return modelAndView;
    }

    @GetMapping("/car_delete_all")
    public String carDeleteAll(@RequestParam(name = "reg_table") String regTable) {
        if (carService.deleteCarByRegTable(regTable))
            return "Delete/car_deleted";
        return "Delete/car_deleted_failed";
    }

    @GetMapping("/car_delete_details")
    public String carDeleteDetails(@RequestParam(name = "reg_table") String reg) {
        return "Delete/get_car_to_delete_details";
    }

}
