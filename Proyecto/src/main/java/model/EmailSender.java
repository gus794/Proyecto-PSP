package model;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailSender {
    final static String senderEmail = "pablokid08@gmail.com";
    final static String senderPassword = "jsqy rdbd vgwa eyjw";
    final static String emailSMTPserver = "smtp.gmail.com";
    final static String emailServerPort = "587";

    public void sender(String nombre) {
        Properties props = new Properties();
        props.put("mail.smtp.user", senderEmail);
        props.put("mail.smtp.host", emailSMTPserver);
        props.put("mail.smtp.port", emailServerPort);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port", emailServerPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.connectiontimeout", "5000");
        try {
            Authenticator auth = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication(){
                    return new PasswordAuthentication(senderEmail, senderPassword);
                }
            };
            Session session = Session.getInstance(props, auth);
            Message msg = new MimeMessage(session);
            msg.setText("Sample message");
            msg.setSubject("Send by "+nombre);
            msg.setFrom(new InternetAddress(senderEmail));
            msg.addRecipient(Message.RecipientType.TO, new InternetAddress("pablokid08@gmail.com"));
            Transport.send(msg);
            System.out.println("Email sent successfully");
        } catch (Exception ex) {
            System.err.println("Error occurred while sending: " + ex.getMessage());
        }
    }
}