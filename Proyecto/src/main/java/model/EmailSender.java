package model;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class EmailSender {
    final static String senderEmail = "pablokid08@gmail.com";
    final static String senderPassword = "jsqy rdbd vgwa eyjw";
    final static String emailSMTPserver = "smtp.gmail.com";
    final static String emailServerPort = "587";

    public void sender(String nombre, String filePath) {
        Task<Void> emailTask = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Properties props = new Properties();
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.host", emailSMTPserver);
                props.put("mail.smtp.port", emailServerPort);
                props.put("mail.smtp.ssl.trust", emailSMTPserver);

                Session session = Session.getInstance(props, new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail, senderPassword);
                    }
                });

                try {
                    MimeMessage msg = new MimeMessage(session);
                    msg.setFrom(new InternetAddress(senderEmail));
                    msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse("pablokid08@gmail.com"));
                    if(filePath != ""){
                        msg.setSubject("Payroll " + nombre);
                        msg.setText("This is the payroll for " + nombre);

                        MimeBodyPart filePart = new MimeBodyPart();
                        filePart.attachFile(new File(filePath));

                        Multipart multipart = new MimeMultipart();
                        multipart.addBodyPart(filePart);

                        msg.setContent(multipart);
                    }else{
                        msg.setSubject("Tasks for " + nombre);
                        MimeBodyPart htmlPart = new MimeBodyPart();
                        htmlPart.setContent(
                                "<div style=\" padding: 30px;heigth: 200; width: 400px; border: 2px solid black;\">\n" +
                                        "        <h1>You have new asignments</h1>\n" +
                                        "        <p>Check your mobile app.</p>\n" +
                                        "    </div>", "text/html");
                        Multipart multipart = new MimeMultipart();
                        multipart.addBodyPart(htmlPart);
                        msg.setContent(multipart);
                    }

                    Transport.send(msg);
                    System.out.println("Email sent successfully");

                } catch (MessagingException | IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
        new Thread(emailTask).start();
    }
}