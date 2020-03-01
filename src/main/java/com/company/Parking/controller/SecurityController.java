package com.company.Parking.controller;

import com.company.Parking.model.AppUser;
import com.company.Parking.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class SecurityController {

    AppUserService appUserService;

    @Autowired
    public SecurityController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @RequestMapping("/login")
    public String login(){
        return "Security/login";
    }

    @RequestMapping("/logout")
    public String logout(){
        return "redirect:/";
    }

    @GetMapping("/logout_form")
    public String logoutForm(){
        return "Security/logout_form";
    }

    @GetMapping("/register_form")
    public ModelAndView registerForm(){
        return new ModelAndView("Security/register_form", "user", new AppUser());
    }

    @PostMapping("/register")
    public String register(@ModelAttribute AppUser user, Errors errors){
        if(errors.hasErrors())
            return "Security/register_data_not_correct";
        else if(appUserService.existsByUserName(user.getUsername()))
            return "Security/register_username_already_exists";
        else if(appUserService.existsByEmail(user.getEmail()))
            return "Security/register_email_already_exists";
        else {
            appUserService.createUser(user);
            return "Security/register_successful";
        }
    }


}
