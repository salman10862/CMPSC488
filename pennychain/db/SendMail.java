package pennychain.db;

//import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

    public static void main(String[] args) {
    	SendMail mailObj = new SendMail();
    	mailObj.sendEmail("pranav726@live.com", "Sample Project");
    }
    
    public void sendEmail(String to, String projName){

    	//Google email credentials
        final String username = "orteam123@gmail.com";	//default email account for this project
        final String password = "pennychain1$";			//password for email account

        //Set system properties for sending email
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        //Authenticate sender's credentials
        Session session = Session.getInstance(props,
          new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
          });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
            msg.setSubject("Project Updated");
            msg.setText("The project " + projName + " has recently been updated."
            		+ " Please log in to view these changes."
                    + "\n\n ~The Penny Chain Team");

            Transport.send(msg);

            System.out.println("Email Sent!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    //email to be sent when project is shared with another user
    public void sendShareEmail(String to, String sender){   //sender = sender's first name or username
        //Google email credentials
        final String username = "orteam123@gmail.com";	//default email account for this project
        final String password = "pennychain1$";			//password for email account

        //Set system properties for sending email
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        //Authenticate sender's credentials
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            msg.setSubject("A project has been shared with you");
            msg.setText(sender + "has shared a project with you. Please log in to view it."
                    + "\n\n~The Penny Chain Team");

            Transport.send(msg);

            System.out.println("Email Sent!");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
