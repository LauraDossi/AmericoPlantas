package model;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class ServicoDeEmail {

    private final String remetente = "leiteninhocombanana@gmail.com";
    private final String senhaApp  = "ifvwrtjxtpmixthe";

    public boolean enviarNovaSenha(String emailDestino, String novaSenha) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remetente, senhaApp);
            }
        });

        try {
            Message msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(remetente));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestino));
            msg.setSubject("Recuperação de senha");
            msg.setText("Sua nova senha é: " + novaSenha +
                    "\n\nPor favor, altere-a após o login.");

            Transport.send(msg);
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}