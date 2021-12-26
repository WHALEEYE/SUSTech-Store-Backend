package tech.whaleeye.misc.utils;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.security.GeneralSecurityException;
import java.util.Properties;

import static tech.whaleeye.misc.constants.Values.STATIC_RESOURCE_PATH;

public class EmailUtils {
    private static final String EMAIL = System.getenv("SUSTechStoreEmail");
    private static final String EMAIL_PWD = System.getenv("SUSTechStoreEmailPWD");
    private static final String EMAIL_SVR = System.getenv("SUSTechStoreEmailSVR");
    // TODO: make the email template prettier
    private static final String EMAIL_TEMPLATE = "<body style=\"background-color:#FFAA43\">\n" +
            "<style type=\"text/css\">\n" +
            "\n" +
            "    .center-in-center {\n" +
            "        position: absolute;\n" +
            "        top: 27%;\n" +
            "        left: 33%;\n" +
            "    }\n" +
            "</style>\n" +
            "\n" +
            "<div id=\"container\" class=\"center-in-center\" style=\"width:1000px;background-color:#eeeeee;position:center;\">\n" +
            "    <div id=\"header\" style=\"background-color:#111111;height: 100px;width:1000px\">\n" +
            "        <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"230\" height=\"95\" viewBox=\"0 0 618 200\">\n" +
            "            <defs>\n" +
            "                <style>\n" +
            "                    .cls-1 {\n" +
            "                        fill: #fff;\n" +
            "                        fill-rule: evenodd;\n" +
            "                    }\n" +
            "                </style>\n" +
            "            </defs>\n" +
            "            <path id=\"Deal_\" data-name=\"Deal!\" class=\"cls-1\" d=\"M157,45.5l-37.168.833-2.167,13.834L129.168,65l-12.5,79.5-13,4L101.333,163h47c40,0,67-27,67-75.836C215.337,59.83,198.5,45.5,172.5,45.5H157Zm-15.667,98,12.5-78.335h15.334c13.667,0,20,10.334,20,23,0,28.668-12,55.335-37,55.335H141.335ZM305.669,95.165c0-14.5-12-23.834-31.668-23.834-27.334,0-50.168,25.668-50.168,62.5,0,19.5,13.167,30.835,33.168,30.835,17.667,0,34.334-9.5,39.168-12.834l-7-12.334c-5,2-15.5,6-25.335,6s-15.667-3.5-15.667-18C284.835,127.166,305.669,114,305.669,95.165Zm-24.167,3c0,7.5-10.167,14.167-31.335,14.667,3.334-14.667,14-22.834,23-22.834C277.835,90,281.5,92.665,281.5,98.165Zm118.832,63.168,2.167-13.167-12.834-2.5L401.334,73H386.167l-4.834,4.5a34.6,34.6,0,0,0-19.5-6.167c-25.834,0-51,21.5-51,63.168,0,16.834,9.167,30.168,23.834,30.168,21,0,34.835-24.668,35.668-26.334L364.5,163Zm-28.5-56.168s-11.167,40-27.834,40c-5,0-8.167-5-8.167-13.834,0-24.167,11-42,26.334-42a34.676,34.676,0,0,1,12.167,2Zm71.5,40.334,17.834-111.67L423.83,35.163,421.663,48.5,434.5,51.83,416.663,163l37.168-1.667L456,148Zm37.5-25.167,19.334-.833,16.334-84H491.83Zm-9.167,33c0,7.334,4.833,11.834,12.167,11.834,10.834,0,16.167-8.5,16.167-18.167,0-7.334-4.667-12-12-12C477,135,471.663,143.666,471.663,153.333Z\"/>\n" +
            "        </svg>\n" +
            "\n" +
            "    </div>\n" +
            "\n" +
            "\n" +
            "\n" +
            "\n" +
            "    <div id=\"content\" style=\"background-color:#FFFFFF;height:600px;width:900px;float:left;margin-left: 50px\">\n" +
            "        <font size=\"9\" > <br><b>You are binding your card number to your account.</b></font>\n" +
            "        <center><font size=\"8\" ><br>Your <b>verification code</b> is<br> <u\n" +
            "                style=\"color: blue\">%s</u></font>\n" +
            "            <br><br></center>\n" +
            "        <font size=\"6.5\">Verification codes expire after <strong>5</strong> minutes.<br><br>If this\n" +
            "            wasn't you, You can ignore this message. There's no need to take any action.</font>\n" +
            "\n" +
            "    </div>\n" +
            "\n" +
            "    <div id=\"footer\" style=\"background-color:#111111;clear:both;text-align:center;height:46px\">\n" +
            "\n" +
            "\n" +
            "</div>\n" +
            "</div>\n" +
            "</body>";

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
        DataHandler handler = new DataHandler(new FileDataSource(new File(STATIC_RESOURCE_PATH, "logo.png")));
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

