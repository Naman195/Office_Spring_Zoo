package com.example.naman.services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * JavaSmtpGmailSender Service
 * @author Naman Arora
 *
 * @since 30-dec-2024
  */

@Service
public class JavaSmtpGmailSenderService {

	@Autowired
    private JavaMailSender emailSender;
	
	/**
	 * this method is used for send Email for OTP.
	 * @param toEmail, subject, body
	 * @return void
	 * 
	 * @author Naman Arora
	 * */
	
	 public void sendEmail(String toEmail, String subject, String body){
	        SimpleMailMessage message = new SimpleMailMessage();
	        message.setFrom("naman.arora1905@gmail.com");
	        message.setTo(toEmail);
	        message.setSubject(subject);
	        message.setText(body);

	        emailSender.send(message);

	        System.out.println("Message sent successfully");
	    }
}
