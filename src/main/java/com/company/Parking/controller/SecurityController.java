package com.company.Parking.controller;

import com.company.Parking.model.AppUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SecurityController {

    @RequestMapping("/login")
    public String login(){
        return "Security/login";
    }

    @GetMapping("/register_form")
    public ModelAndView registerForm(){
        return new ModelAndView("Security/register_form", "user", new AppUser());
    }

    @PostMapping("/register")
    public String register(@ModelAttribute AppUser user){
        System.out.println(user);
        return "redirect:/register_form";
    }


}
