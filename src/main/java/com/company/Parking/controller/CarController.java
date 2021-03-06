package com.company.Parking.controller;

import com.company.Parking.model.Car;
import com.company.Parking.model.Report;
import com.company.Parking.service.CarRepoService;
import com.company.Parking.service.CarService;
import com.company.Parking.service.ConvertionService;
import com.company.Parking.service.JavaMailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/")
@Slf4j
public class CarController {

    CarRepoService carRepoService;
    CarService carService;
    JavaMailSenderService javaMailSenderService;

    @Autowired
    public CarController(CarRepoService carRepoService, CarService carService, JavaMailSenderService javaMailSenderService) {
        this.carRepoService = carRepoService;
        this.carService = carService;
        this.javaMailSenderService = javaMailSenderService;
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


    @PostMapping("/added")
    public String addedNewCar(@ModelAttribute @Valid Car car, Errors errors, Principal principal) {
        if (errors.hasErrors())
            return "Add/add_failure_data_incorrect";
        else if (!carRepoService.createCar(car, new Report(principal.getName()))){
            return "Add/entry_already_added";
        }

        javaMailSenderService.sendNotifyAboutNewCar(car);
        return "redirect:/";
    }

    @GetMapping("/car_delete")
    public ModelAndView carDelete() {
        ModelAndView modelAndView = new ModelAndView("Delete/get_car_to_delete");
        modelAndView.addObject("regs", carRepoService.getRegsList());
        return modelAndView;
    }


    //ADD Permission
    @GetMapping("/car_delete_all")
    public String carDeleteAll(@RequestParam(name = "reg_table") String regTable) {
        if (carRepoService.deleteCarByRegTable(regTable))
            return "Delete/car_deleted";
        return "Delete/car_deleted_failed";
    }

    //ADD Permission
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

    //ADD Permission
    @GetMapping("/car_delete_details_deleted")
    public String carDeleteDetailDeleted(@RequestParam(name = "reportDate") String reportDate, @RequestParam(name = "regTable") String regTable) {
        if (carRepoService.deleteReportFromCar(regTable, ConvertionService.stringToLocalDate(reportDate)))
            return "Delete/car_deleted";
        return "Delete/car_deleted_failed";
    }


    @GetMapping("/car_modify")
    public ModelAndView carModify() {
        ModelAndView modelAndView = new ModelAndView("Modify/get_car_to_modify");
        modelAndView.addObject("regs", carRepoService.getRegsList());
        return modelAndView;
    }

    @GetMapping("/car_modify_details")
    public ModelAndView carModifyDetails(@RequestParam(name = "reg_table") String regTable) {
        ModelAndView modelAndView = new ModelAndView("Modify/car_modify_failed");
        Optional<Car> car = carRepoService.getCarByRegTable(regTable);
        if (car.isPresent()) {
            modelAndView.setViewName("Modify/get_car_to_modify_details");
            modelAndView.addObject("carDetails", car.get());
        }
        return modelAndView;
    }

    @GetMapping("/car_modify_details_modified")
    public String carModifyDetailsModified(@RequestParam(value = "regTable") String regTable, @RequestParam(value = "mark") String mark,
                                           @RequestParam(value = "model") String model, @RequestParam(value = "color") String color) {
        Optional<Car> optionalCar = carRepoService.getCarByRegTable(regTable);

        if (optionalCar.isPresent()) {
            Car car = optionalCar.get();
            car.setMark(mark);
            car.setModel(model);
            car.setColor(color);
            return carRepoService.saveModifiedCar(car) ? "Modify/car_modified" : "Modify/car_modify_failed";
        }
        return "Modify/car_modify_failed";
    }

    @GetMapping("/car_search")
    public ModelAndView carSearch() {
        ModelAndView modelAndView = new ModelAndView("Search/get_car_to_search");
        modelAndView.addObject("fields", carService.getFieldsName());
        return modelAndView;
    }

    @GetMapping("/car_search_result")
    public ModelAndView carSearchResult(@RequestParam(value = "field") String field, @RequestParam(value = "content") String content) {
        ModelAndView modelAndView = new ModelAndView("Search/get_car_to_search_result");
        modelAndView.addObject("cars", carRepoService.searchCarByField(field, content));
        return modelAndView;
    }


}
