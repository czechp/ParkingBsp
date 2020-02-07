package com.company.Parking.controller;

import com.company.Parking.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class CarController {

    CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public ModelAndView main(){
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("cars", carService.findAllCar());
        return modelAndView;
    }
    
}
