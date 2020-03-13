package com.company.Parking.controller;

import com.company.Parking.model.AppUser;
import com.company.Parking.model.UserRoles;
import com.company.Parking.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class AppUserController {
    private AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/users")
    public ModelAndView getUsers() {
        ModelAndView modelAndView = new ModelAndView("Users/users");
        modelAndView.addObject("users", appUserService.findAll());
        return modelAndView;
    }

    @GetMapping("/admin_panel")
    public ModelAndView getAdminPanel() {
        ModelAndView modelAndView = new ModelAndView("Users/admin_panel");
        modelAndView.addObject("enabledUsers", appUserService.findAllByAdminVerification(true));
        modelAndView.addObject("disabledUsers", appUserService.findAllByAdminVerification(false));
        return modelAndView;
    }

    @GetMapping("/enable_user")
    public String enableUser(@RequestParam(name = "id") long id) {

        return appUserService.enableUser(id, true) ? "Users/user_modify_success" : "Users/user_modify_failed";
    }

    @GetMapping("/disable_user")
    public String disableUser(@RequestParam(name = "id") long id) {
        return appUserService.enableUser(id, false) ? "Users/user_modify_success" : "Users/user_modify_failed";
    }

    @GetMapping("/set_admin")
    public String setAdmin(@RequestParam(name = "id") long id) {
        return appUserService.setRole(id, UserRoles.ADMIN) ? "Users/user_modify_success" : "Users/user_modify_failed";
    }

    @GetMapping("/set_user")
    public String setUser(@RequestParam(name = "id") long id) {
        return appUserService.setRole(id, UserRoles.USER) ? "Users/user_modify_success" : "Users/user_modify_failed";
    }

    @GetMapping("/delete_user")
    public String deleteUser(@RequestParam(name = "id") long id) {
        return appUserService.deleteById(id) ? "Users/user_modify_success" : "Users/user_modify_failed";
    }
}
