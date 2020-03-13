package com.company.Parking.service;

import com.company.Parking.model.AppUser;
import com.company.Parking.model.Car;
import com.company.Parking.model.UserRoles;
import com.company.Parking.model.VerificationToken;
import com.company.Parking.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;
import java.util.List;

@Service
public class JavaMailSenderService {

    private JavaMailSender javaMailSender;
    private String subject;
    private String content;
    private AppUserService appUserService;

    @Autowired
    public JavaMailSenderService(JavaMailSender javaMailSender, AppUserService appUserService) {
        this.javaMailSender = javaMailSender;
        this.appUserService = appUserService;
    }

    public void sendNotifyAboutNewCar(Car car) {
        List<AppUser> users = appUserService.findAll();
        for (AppUser user : users) {
            subject = "Nieautoryzowane pozostawienie auta na parkingu Automatyków";
            content = "Marka: " + car.getMark() + "\n" +
                    "Model: " + car.getModel() + " \n" +
                    "Nr. rejestracyjny: " + car.getRegTable() + "\n" +
                    "Kolor: " + car.getColor() + "\n" +
                    "Ilość nieautoryzowanych parkowań: " + car.getAmountPark() + "\n\n" +
                    "Więcej szczególów w aplikacji.";
            new Thread(new EmailSenderThread(javaMailSender, user.getEmail(), subject, content)).start();
        }
    }

    public void sendNotifyAboutNewUser(AppUser user) {
        List<AppUser> admins = appUserService.findAllByRole(UserRoles.ADMIN);
        for (AppUser admin : admins) {
            subject = "Nowy użytkownik w serwsie ===Parking automatycy===";
            content = "W serwisie zarejstrował się nowy użytkownik ---> " + user.getUsername() + " <----- \n" +
                    "Zweryfikuj tożsamość oraz zatwierdz.";

            new Thread(new EmailSenderThread(javaMailSender, admin.getEmail(), subject, content)).start();
        }
    }

    public void sendVerification(AppUser user, VerificationToken token, ServletRequest request) {
        subject = "Parking automatycy - weryfikacja konta";
        content = "Aby aktywować konto użyj nastepującego linku - " + "http://" + request.getServerName() +
                ":" + request.getServerPort() + "/verify_token?token=" + token.getToken();
        new Thread(new EmailSenderThread(javaMailSender, user.getEmail(), subject, content)).start();
    }
}
