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

    // TODO: Change it into a Util Method
    // TODO: Convert all the comments into English
    public static void main(String[] args) throws GeneralSecurityException, MessagingException {
        Properties prop = new Properties();
        prop.setProperty("mail.host", EMAIL_SVR);  // 设置QQ邮件服务器
        prop.setProperty("mail.transport.protocol", "smtp"); // 邮件发送协议
        prop.setProperty("mail.smtp.auth", "true"); // 需要验证用户名密码

        // QQ邮箱设置SSL加密
        MailSSLSocketFactory sf = new MailSSLSocketFactory();
        sf.setTrustAllHosts(true);
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);

        //1、创建定义整个应用程序所需的环境信息的 Session 对象
        Session session = Session.getDefaultInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                //传入发件人的姓名和授权码
                return new PasswordAuthentication(EMAIL, EMAIL_PWD);
            }
        });

        //2、通过session获取transport对象
        Transport transport = session.getTransport();

        //3、通过transport对象邮箱用户名和授权码连接邮箱服务器
        transport.connect(EMAIL_SVR, EMAIL, EMAIL_PWD);

        //4、创建邮件,传入session对象
        MimeMessage mimeMessage = complexEmail(session);

        //5、发送邮件
        transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

        //6、关闭连接
        transport.close();


    }

    public static MimeMessage complexEmail(Session session) throws MessagingException {
        //消息的固定信息
        MimeMessage mimeMessage = new MimeMessage(session);

        //发件人
        mimeMessage.setFrom(new InternetAddress(EMAIL));
        //收件人
        mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress("11911413@mail.sustech.edu.cn"));
        //邮件标题
        mimeMessage.setSubject("[Deal!] Your Verification Code");

        //邮件内容
        //准备图片数据
        MimeBodyPart image = new MimeBodyPart();
        DataHandler handler = new DataHandler(new FileDataSource("misc\\src\\main\\resources\\logo_temp.png"));
        image.setDataHandler(handler);
        image.setContentID("logo.png"); //设置图片id

        //准备文本
        MimeBodyPart text = new MimeBodyPart();
        text.setContent("<div style=\"margin: auto;text-align: left\"><img src='cid:logo.png' style=\"transform: scale(0.4)\" alt=\"wrong\"></div>\n" +
                "<div>\n" +
                "    Your verification code is <u style=\"color: blue\">990099</u>, please enter.\n" +
                "</div>\n", "text/html;charset=utf-8");

        //拼装邮件正文
        MimeMultipart mimeMultipart = new MimeMultipart();
        mimeMultipart.addBodyPart(image);
        mimeMultipart.addBodyPart(text);
        mimeMultipart.setSubType("related");//文本和图片内嵌成功

//        //附件
//        MimeBodyPart appendix = new MimeBodyPart();
//        appendix.setDataHandler(new DataHandler(new FileDataSource("")));
//        appendix.setFileName("test.txt");



//        //将拼装好的正文内容设置为主体
//        MimeBodyPart contentText = new MimeBodyPart();
//        contentText.setContent(mimeMultipart);
//
//        //拼接附件
//        MimeMultipart allFile = new MimeMultipart();
//        allFile.addBodyPart(appendix);//附件
//        allFile.addBodyPart(contentText);//正文
//        allFile.setSubType("mixed"); //正文和附件都存在邮件中，所有类型设置为mixed
//
//
        //放到Message消息中
        mimeMessage.setContent(mimeMultipart);
        mimeMessage.saveChanges();//保存修改

        return mimeMessage;
    }
}

