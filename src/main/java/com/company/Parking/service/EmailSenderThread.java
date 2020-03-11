package com.company.Parking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
public class EmailSenderThread implements Runnable {

    private JavaMailSender javaMailSender;
    private String addresss;
    private String subject;
    private String content;

    public EmailSenderThread(JavaMailSender javaMailSender, String addresss, String subject, String content) {
        this.javaMailSender = javaMailSender;
        this.addresss = addresss;
        this.subject = subject;
        this.content = content;
    }

    @Override
    public void run() {
        sendMail();
    }

    private void sendMail(){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(addresss);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(content);

        try {
            javaMailSender.send(simpleMailMessage);
            log.info("Email to " + addresss + " has sent correct");
        } catch (MailException e) {
            log.error("Error during sending e-mail to " + addresss, e);
        }
    }
}
