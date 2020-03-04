package com.company.Parking.service;

import com.company.Parking.model.AppUser;
import com.company.Parking.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.ServletRequest;

@Service
public class JavaMailSenderService {

    private JavaMailSender javaMailSender;
    private String subject;
    private String content;


    @Autowired
    public JavaMailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerification(AppUser user, VerificationToken token, ServletRequest request) {
        subject = "Parking automatycy - weryfikacja konta";
        content = "Aby aktywować konto użyj nastepującego linku - " + "http://" + request.getServerName() +
                ":" + request.getServerPort() + "/verify_token?token=" + token.getToken();
        new Thread(new EmailSenderThread(javaMailSender, user.getEmail(), subject, content)).start();
    }
}
