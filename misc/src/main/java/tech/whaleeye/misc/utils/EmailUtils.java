package tech.whaleeye.misc.utils;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.security.GeneralSecurityException;
import java.util.Properties;

public class EmailUtils {
    private static final String EMAIL = System.getenv("SUSTechStoreEmail");
    private static final String EMAIL_PWD = System.getenv("SUSTechStoreEmailPWD");
    private static final String EMAIL_SVR = System.getenv("SUSTechStoreEmailSVR");
    // TODO: make the email template prettier
    private static final String EMAIL_TEMPLATE = "";

    public static void sendVCodeEmail(String receiveEmail, String vCode) throws GeneralSecurityException, MessagingException {
        Properties prop = new Properties();
        // set the mail server
        prop.setProperty("mail.host", EMAIL_SVR);
        // set the protocol of sending emails
        prop.setProperty("mail.transport.protocol", "smtp");
        // need to authorize
        prop.setProperty("mail.smtp.auth", "true");

        // set ssl encryption
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);

        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Set the email address and password of the sender
                return new PasswordAuthentication(EMAIL, EMAIL_PWD);
            }
        });

        Transport transport = session.getTransport();

        transport.connect(EMAIL_SVR, EMAIL, EMAIL_PWD);

        MimeMessage mimeMessage = complexEmail(session, receiveEmail, vCode);

        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

        transport.close();
    }

    public static MimeMessage complexEmail(Session session, String receiveEmail, String VCode) throws MessagingException {

        MimeMessage mimeMessage = new MimeMessage(session);

        // set sender
        mimeMessage.setFrom(new InternetAddress(EMAIL));
        // set receiver
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(receiveEmail));
        // set subject
        mimeMessage.setSubject("[Deal!] Your Verification Code");

        // set the content of the email
        // prepare pictures
        MimeBodyPart image = new MimeBodyPart();
        DataHandler handler = new DataHandler(new FileDataSource("misc\\src\\main\\resources\\logo_temp.png"));
        image.setDataHandler(handler);
        // set the id of the picture (can be used by cid)
        image.setContentID("logo.png");

        // prepare text
        MimeBodyPart text = new MimeBodyPart();
        text.setContent(String.format("<div style=\"margin: auto;text-align: left\"><img src='cid:logo.png' style=\"transform: scale(0.4)\" alt=\"wrong\"></div>\n<div>\n    Your verification code is <u style=\"color: blue\">%s</u>, please enter.\n</div>\n", VCode), "text/html;charset=utf-8");

        // assemble the pictures and the text into an email
        MimeMultipart mimeMultipart = new MimeMultipart();
        mimeMultipart.addBodyPart(image);
        mimeMultipart.addBodyPart(text);
        mimeMultipart.setSubType("related");

//        // prepare appendix
//        MimeBodyPart appendix = new MimeBodyPart();
//        appendix.setDataHandler(new DataHandler(new FileDataSource("")));
//        appendix.setFileName("test.txt");


//        // set the assembled content as the main part
//        MimeBodyPart contentText = new MimeBodyPart();
//        contentText.setContent(mimeMultipart);
//
//        // attach the appendix
//        MimeMultipart allFile = new MimeMultipart();
//        allFile.addBodyPart(appendix);
//        allFile.addBodyPart(contentText);
//        allFile.setSubType("mixed");
//
//
        // put the content into the message
        mimeMessage.setContent(mimeMultipart);

        // save
        mimeMessage.saveChanges();

        return mimeMessage;
    }
}

