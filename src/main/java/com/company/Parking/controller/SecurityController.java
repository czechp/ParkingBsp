package com.company.Parking.controller;

import com.company.Parking.model.AppUser;
import com.company.Parking.model.VerificationToken;
import com.company.Parking.service.AppUserService;
import com.company.Parking.service.JavaMailSenderService;
import com.company.Parking.service.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import java.util.Optional;

@Controller
public class SecurityController {

    AppUserService appUserService;
    VerificationTokenService verificationTokenService;
    JavaMailSenderService javaMailSenderService;

    @Autowired
    public SecurityController(AppUserService appUserService, VerificationTokenService verificationTokenService, JavaMailSenderService javaMailSenderService) {
        this.appUserService = appUserService;
        this.verificationTokenService = verificationTokenService;
        this.javaMailSenderService = javaMailSenderService;
    }

    @RequestMapping("/login")
    public String login() {
        return "Security/login";
    }

    @RequestMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @GetMapping("/logout_form")
    public String logoutForm() {
        return "Security/logout_form";
    }

    @GetMapping("/register_form")
    public ModelAndView registerForm() {
        return new ModelAndView("Security/register_form", "user", new AppUser());
    }

    @PostMapping("/register")
    public String register(@ModelAttribute AppUser user, Errors errors, ServletRequest servletRequest) {
        if (errors.hasErrors())
            return "Security/register_data_not_correct";
        else if (appUserService.existsByUserName(user.getUsername()))
            return "Security/register_username_already_exists";
        else if (appUserService.existsByEmail(user.getEmail()))
            return "Security/register_email_already_exists";
        else {
            appUserService.saveUser(user);
            VerificationToken verificationToken = new VerificationToken(user);
            verificationTokenService.saveNewToken(verificationToken);
            javaMailSenderService.sendVerification(user, verificationToken, servletRequest);
            javaMailSenderService.sendNotifyAboutNewUser(user);
            return "Security/register_successful";
        }
    }

    @GetMapping("/verify_token")
    public String verifyToken(@RequestParam(name = "token") String token) {
        Optional<VerificationToken> optionalVerificationToken = verificationTokenService.findVerificationTokenByToken(token);
        if (optionalVerificationToken.isPresent()) {
            Optional<AppUser> optionalAppUser = appUserService.findById(optionalVerificationToken.get().getId());
            if (optionalAppUser.isPresent()) {
                AppUser appUser =  optionalAppUser.get();
                appUser.setEmailVerification(true);
                appUserService.saveUser(appUser);

                System.out.println(appUserService.findById(optionalVerificationToken.get().getAppUser().getId()));

                return "Security/verify_token_success";
            }
        }
        return "Security/verify_token_failed";

    }

}
