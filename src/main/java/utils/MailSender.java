package utils;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailSender implements Runnable {
    private Thread t;

    private String from, to, title, content;
    private Properties props;
    private Session session;

    public MailSender(String from, String to, String title, String content) {
        this.from = from;
        this.to = to;
        this.title = title;
        this.content = content;

        setupProperties();
        setupSession();
    }

    private void setupProperties() {
        props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class",
                "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
    }

    private void setupSession() {
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("nope","you_thought");
                    }
                });
    }

    public void run() {
        try {
            System.out.println("Sending to " + to + " a mail from " + from);
            System.out.println("Content: " + content);
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));
            message.setSubject(title);
            message.setText(content);

            Transport.send(message);

            System.out.println("Done");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        if(t == null) {
            t = new Thread(this);
            t.start();
        }
    }
}
