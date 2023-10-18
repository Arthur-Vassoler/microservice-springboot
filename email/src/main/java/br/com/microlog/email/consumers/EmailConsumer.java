package br.com.microlog.email.consumers;

import br.com.microlog.email.dtos.EmailRecordDto;
import br.com.microlog.email.models.EmailModel;
import br.com.microlog.email.services.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {
  final EmailService emailService;

  public EmailConsumer(EmailService emailService) {
    this.emailService = emailService;
  }

  @RabbitListener(queues = "${broker.queue.email.name}")
  public void listenEmailQueue(@Payload EmailRecordDto emailRecordDto) {
    var emailModel = new EmailModel();
    BeanUtils.copyProperties(emailRecordDto, emailModel);

    emailService.sendEmail(emailModel);
  }
}
