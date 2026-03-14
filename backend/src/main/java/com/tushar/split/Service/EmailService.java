package com.tushar.split.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    JavaMailSender mailSender;


    public void sendMail(String otp, String email) {

        SimpleMailMessage message=new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Account Verification");

        message.setText(
                "Your OTP for email verification is: " + otp +
                        "\n\nThis OTP will expire in 5 minutes."
        );
        mailSender.send(message);
    }
}
