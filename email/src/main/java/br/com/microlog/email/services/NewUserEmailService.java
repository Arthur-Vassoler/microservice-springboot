package br.com.microlog.email.services;

import br.com.microlog.email.enums.StatusEmail;
import br.com.microlog.email.models.NewUserEmailModel;
import br.com.microlog.email.repositories.NewUserEmailRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class NewUserEmailService {
  final NewUserEmailRepository newUserEmailRepository;
  final JavaMailSender emailSender;

  public NewUserEmailService(NewUserEmailRepository newUserEmailRepository, JavaMailSender emailSender) {
    this.newUserEmailRepository = newUserEmailRepository;
    this.emailSender = emailSender;
  }

  @Value(value = "${spring.mail.username}")
  private String emailFrom;

  @Transactional
  public NewUserEmailModel sendNewUserEmail(NewUserEmailModel newUserEmailModel) {
    try {
      var subject = "Bem-vindo a Plataforma da Microlog";
      String text = null;

      newUserEmailModel.setSendDateEmail(LocalDateTime.now());
      newUserEmailModel.setEmailFrom(emailFrom);

      ClassPathResource resource = new ClassPathResource("emailsTemplates/new_user_email_template.html");

      try (InputStream is = resource.getInputStream()) {
        text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      }

      text = text.replace("username", newUserEmailModel.getUsername());

      MimeMessage mimeMessage = emailSender.createMimeMessage();
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

      message.setTo(newUserEmailModel.getEmailTo());
      message.setSubject(subject);
      message.setText(text, true);

      emailSender.send(mimeMessage);

      newUserEmailModel.setStatusEmail(StatusEmail.SENT);
    } catch (MailException | IOException e) {
      newUserEmailModel.setStatusEmail(StatusEmail.ERROR);
    } finally {
      return newUserEmailRepository.save(newUserEmailModel);
    }
  }
}
