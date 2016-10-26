package com.anex13.dipapp;

/**
 * Created by it.zavod on 21.10.2016.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class GMail {

    String emailPort;// gmail's smtp port
    String smtpAuth;
    String starttls;
    String emailHost;


    String fromEmail;
    String fromPassword;
    List<String> toEmailList;
    String emailSubject;
    String emailBody;

    Properties emailProperties;
    Session mailSession;
    MimeMessage emailMessage;

    public GMail() {

    }

    public GMail(String emailBody, Context context) {
        SharedPreferences sPref = context.getSharedPreferences(FragPrefs.PREF_TAG, Context.MODE_PRIVATE);
        if (sPref.getBoolean(FragPrefs.USE_GMAIL, false)) {
            this.emailPort = "587";// gmail's smtp port
            this.smtpAuth = "true";
            this.starttls = "true";
            this.emailHost = "smtp.gmail.com";
        } else {
            this.emailPort = sPref.getString(FragPrefs.MAIL_PORT, "587");
            this.smtpAuth = Boolean.toString(sPref.getBoolean(FragPrefs.USE_AUTH, false));
            this.starttls = Boolean.toString(sPref.getBoolean(FragPrefs.USE_TLS, false));
            this.emailHost = sPref.getString(FragPrefs.MAIL_URL, "smtp.gmail.com");
        }
       /* this.fromEmail = "it.zavod.bulbash@gmail.com";
        this.fromPassword = "742617000027";
        String toEmails = "it.zavod@bulbash.com";*/
        this.fromEmail = sPref.getString(FragPrefs.MAIL_FROM,"");
        this.fromPassword = sPref.getString(FragPrefs.MAIL_PASS,"");
        String toEmails = sPref.getString(FragPrefs.MAIL_TO,"");
        this.toEmailList = Arrays.asList(toEmails
                .split("\\s*,\\s*"));
        Log.i("SendMailActivity", "To List: " + toEmailList);
        //String emailSubject = "servers error";
        this.emailSubject = sPref.getString(FragPrefs.MAIL_HEAD,"Houston we have a problem");
        this.emailBody = emailBody;

        emailProperties = System.getProperties();
        emailProperties.put("mail.smtp.port", emailPort);
        emailProperties.put("mail.smtp.auth", smtpAuth);
        emailProperties.put("mail.smtp.starttls.enable", starttls);
        Log.i("GMail", "Mail server properties set.");
    }

    public MimeMessage createEmailMessage() throws AddressException,
            MessagingException, UnsupportedEncodingException {

        mailSession = Session.getDefaultInstance(emailProperties, null);
        emailMessage = new MimeMessage(mailSession);

        emailMessage.setFrom(new InternetAddress(fromEmail, fromEmail));
        for (String toEmail : toEmailList) {
            Log.i("GMail", "toEmail: " + toEmail);
            emailMessage.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(toEmail));
        }

        emailMessage.setSubject(emailSubject);
        emailMessage.setContent(emailBody, "text/html");// for a html email
        // emailMessage.setText(emailBody);// for a text email
        Log.i("GMail", "Email Message created.");
        return emailMessage;
    }

    public void sendEmail() throws AddressException, MessagingException {

        Transport transport = mailSession.getTransport("smtp");
        transport.connect(emailHost, fromEmail, fromPassword);
        Log.i("GMail", "allrecipients: " + emailMessage.getAllRecipients());
        transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
        transport.close();
        Log.i("GMail", "Email sent successfully.");
    }

}
// final String emailPort = "587";// gmail's smtp port
// final String smtpAuth = "true";
//final String starttls = "true";
//final String emailHost = "smtp.gmail.com";