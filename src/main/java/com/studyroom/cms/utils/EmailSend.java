package com.studyroom.cms.utils;



import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


import java.util.Date;
import java.util.Properties;

@Component
public class EmailSend {
    private static Session session = null;
    private String auth_password = "crnuxywhhusfbegc";
    private static String sendEmails = "742906522@qq.com";


    /**
     * 发送单个邮件
     * toEmail :收件人list
     * subject :标题list
     * info:信息list
     * @param toEmail
     * @param subject
     * @param info
     * @throws AddressException
     * @throws MessagingException
     */
    public void EmailSendLogic_single(String toEmail,String subject,String info) throws AddressException, MessagingException {
        Properties properties = new Properties();// 配置与服务器连接参数
        properties.setProperty("mail.transport.protocol", "SMTP");//邮件发送协议
        properties.setProperty("mail.host", "smtp.qq.com");//邮件发送服务器的地址（如QQ邮箱的发件服务器地址SMTP服务器: smtp.qq.com）
        properties.setProperty("mail.smtp.auth", "true");//指定验证为true
        // 创建验证器
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("742906522", "crnuxywhhusfbegc");
            }
        };

        Session session = Session.getInstance(properties, auth);

        // make emails
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("742906522@qq.com")); // 设置发送者的邮箱地址
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail)); // 设置发送方式与接收者
        message.setSubject(subject==null?defaultSubject() :subject);//邮件主题
        message.setContent(info==null?defaultEmailMsg():info, "text/html;charset=utf-8");//设置邮件的内容

        Transport.send(message);


    }


    /**
     * 发送复数的邮件
     * toEmail :收件人list
     * subject :标题list
     * info:信息list
     * @param toEmails
     * @param subjects
     * @param infos
     * @throws AddressException
     * @throws MessagingException
     */
    public void EmailSendLogic_multiply(String[] toEmails,String[] subjects,String[] infos)throws AddressException, MessagingException {
        Properties properties = new Properties();// 配置与服务器连接参数
        properties.setProperty("mail.transport.protocol", "SMTP");//邮件发送协议
        properties.setProperty("mail.host", "smtp.qq.com");//邮件发送服务器的地址（如QQ邮箱的发件服务器地址SMTP服务器: smtp.qq.com）
        properties.setProperty("mail.smtp.auth", "true");//指定验证为true
        // 创建验证器
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("742906522", "crnuxywhhusfbegc");
            }
        };
        Session session = Session.getInstance(properties, auth);
        for(int i = 0 ; i < toEmails.length;i++){
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("742906522@qq.com")); // 设置发送者的邮箱地址
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i])); // 设置发送方式与接收者
            message.setSubject(subjects[i]==null?defaultSubject() :subjects[i]);//邮件主题
            message.setContent(infos[i]==null?defaultEmailMsg():infos[i], "text/html;charset=utf-8");//设置邮件的内容
            Transport.send(message);
        }


    }

    public String defaultSubject(){

        return "默认的标题";
    }
    public String defaultEmailMsg(){
        return "默认的信息";
    }


}
