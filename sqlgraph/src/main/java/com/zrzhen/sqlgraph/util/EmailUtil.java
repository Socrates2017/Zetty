package com.zrzhen.sqlgraph.util;

import com.sun.mail.util.MailSSLSocketFactory;
import com.zrzhen.zetty.http.util.ProUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

/**
 * 邮件发送工具类
 *
 * @author ChenAnlian
 */
public class EmailUtil {

    private static final Logger log = LoggerFactory.getLogger(EmailUtil.class);


    private static String account;// 登录用户名
    private static String pass; // 登录密码
    private static String from; // 发件地址
    private static String host; // 服务器地址
    private static String port; // 端口
    private static String protocol; // 协议

    static {
        account = ProUtil.getString("e.account");
        pass = ProUtil.getString("e.pass");
        from = ProUtil.getString("e.from");
        host = ProUtil.getString("e.host");
        port = ProUtil.getString("e.port");
        protocol = ProUtil.getString("e.protocol");

    }

    // 用户名密码验证，需要实现抽象类Authenticator的抽象方法PasswordAuthentication
    static class MyAuthenricator extends Authenticator {
        String u = null;
        String p = null;

        public MyAuthenricator(String u, String p) {
            this.u = u;
            this.p = p;
        }

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(u, p);
        }
    }

    public static void sendEmail(String email, String subject, String content) throws UnsupportedEncodingException, MessagingException {
        Properties prop = new Properties();
        // 协议
        prop.setProperty("mail.transport.protocol", protocol);
        // 服务器
        prop.setProperty("mail.smtp.host", host);
        // 端口
        prop.setProperty("mail.smtp.port", port);
        // 使用smtp身份验证
        prop.setProperty("mail.smtp.auth", "true");
        // 使用SSL，企业邮箱必需！
        // 开启安全协议
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        } catch (GeneralSecurityException e1) {
            e1.printStackTrace();
        }
        prop.put("mail.smtp.ssl.enable", "true");
        prop.put("mail.smtp.ssl.socketFactory", sf);
        //
        Session session = Session.getDefaultInstance(prop, new MyAuthenricator(account, pass));
        session.setDebug(true);
        MimeMessage mimeMessage = new MimeMessage(session);

        mimeMessage.setFrom(new InternetAddress(from, "哲人镇"));
        mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
        mimeMessage.setSubject(subject);
        mimeMessage.setSentDate(new Date());
        // mimeMessage.setText(content);
        mimeMessage.setContent(content, "text/html;charset=utf-8");
        mimeMessage.saveChanges();
        Transport.send(mimeMessage);

    }

}