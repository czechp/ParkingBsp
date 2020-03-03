package com.company.Parking.service;

import com.company.Parking.model.AppUser;
import com.company.Parking.model.VerifiactionToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class JavaMailSenderService {

    private JavaMailSender javaMailSender;
    private String subject;
    private String content;


    @Autowired
    public JavaMailSenderService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendVerification(AppUser user, VerifiactionToken token){
        subject = "Parking automatycy - weryfikacja konta";
        content = "Aby aktywować konto użyj nastepującego linku - " + token.getToken();
        new Thread(new EmailSenderThread(javaMailSender, user.getEmail(), subject, content)).start();
    }
}
