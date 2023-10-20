package br.com.microlog.email.services;

import br.com.microlog.email.enums.StatusEmail;
import br.com.microlog.email.enums.TypeCodeRecoveryEnum;
import br.com.microlog.email.models.CodeRecoveryEmailModel;
import br.com.microlog.email.repositories.CodeRecoveryEmailRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class CodeRecoveryEmailService {
  final CodeRecoveryEmailRepository codeRecoveryEmailRepository;
  final JavaMailSender emailSender;

  public CodeRecoveryEmailService(CodeRecoveryEmailRepository codeRecoveryEmailRepository, JavaMailSender emailSender) {
    this.codeRecoveryEmailRepository = codeRecoveryEmailRepository;
    this.emailSender = emailSender;
  }

  @Value(value = "${spring.mail.username}")
  private String emailFrom;

  @Transactional
  public CodeRecoveryEmailModel sendCodeRecoveryEmail(CodeRecoveryEmailModel codeRecoveryEmailModel) {
    try {
      var subject = (codeRecoveryEmailModel.getTypeCodeRecovery() == TypeCodeRecoveryEnum.USERNAME_PASSWORD_CHANGE)
        ? "Código de recuperação de senha" : "Código de exclusão de usuário";

      String text = null;

      codeRecoveryEmailModel.setSendDateEmail(LocalDateTime.now());
      codeRecoveryEmailModel.setEmailFrom(emailFrom);

      ClassPathResource resource = (codeRecoveryEmailModel.getTypeCodeRecovery() == TypeCodeRecoveryEnum.USERNAME_PASSWORD_CHANGE)
        ? new ClassPathResource("emailsTemplates/code_recovery_email_template.html") : new ClassPathResource("emailsTemplates/code_delete_user_email_template.html");

      try (InputStream is = resource.getInputStream()) {
        text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      }

      text = text.replace("ABC123", codeRecoveryEmailModel.getToken());

      MimeMessage mimeMessage = emailSender.createMimeMessage();
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

      message.setTo(codeRecoveryEmailModel.getEmailTo());
      message.setSubject(subject);
      message.setText(text, true);

      emailSender.send(mimeMessage);

      codeRecoveryEmailModel.setStatusEmail(StatusEmail.SENT);
    } catch (MailException | IOException e) {
      codeRecoveryEmailModel.setStatusEmail(StatusEmail.ERROR);
    } finally {
      return codeRecoveryEmailRepository.save(codeRecoveryEmailModel);
    }
  }

  @Transactional
  public CodeRecoveryEmailModel sendMessageSuccessfullyActionCodeRecoveryEmail(CodeRecoveryEmailModel codeRecoveryEmailModel) {
    try {
      var subject = (codeRecoveryEmailModel.getTypeCodeRecovery() == TypeCodeRecoveryEnum.USERNAME_PASSWORD_CHANGE)
        ? "Senha atlterada com sucesso" : "Usuário excluido com sucesso";

      String text = null;

      ClassPathResource successfullyResource = (codeRecoveryEmailModel.getTypeCodeRecovery() == TypeCodeRecoveryEnum.USERNAME_PASSWORD_CHANGE)
        ? new ClassPathResource("emailsTemplates/user_password_changed_successfully_email_template.html") : new ClassPathResource("emailsTemplates/user_delete_successfully_email_template.html");

      try (InputStream is = successfullyResource.getInputStream()) {
        text = new String(is.readAllBytes(), StandardCharsets.UTF_8);
      }

      MimeMessage mimeMessageSuccessfully = emailSender.createMimeMessage();
      MimeMessageHelper messageSuccessfully = new MimeMessageHelper(mimeMessageSuccessfully, true, "UTF-8");

      messageSuccessfully.setTo(codeRecoveryEmailModel.getEmailTo());
      messageSuccessfully.setSubject(subject);
      messageSuccessfully.setText(text, true);

      emailSender.send(mimeMessageSuccessfully);
    } catch (MailException | IOException e) {
      codeRecoveryEmailModel.setStatusEmail(StatusEmail.ERROR);
    } finally {
      return codeRecoveryEmailRepository.save(codeRecoveryEmailModel);
    }
  }
}
