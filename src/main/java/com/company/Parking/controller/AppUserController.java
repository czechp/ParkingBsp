package com.company.Parking.controller;

import com.company.Parking.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUserController {
    private AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/users")
    public ModelAndView getUsers(){
        ModelAndView modelAndView  = new ModelAndView("Users/users");
        modelAndView.addObject("users", appUserService.findAll());
        return modelAndView;
    }
}
