package br.com.microlog.email.consumers;

import br.com.microlog.email.dtos.CodeRecoveryEmailRecordDto;
import br.com.microlog.email.dtos.EmailRecordDto;
import br.com.microlog.email.models.CodeRecoveryEmailModel;
import br.com.microlog.email.models.NewUserEmailModel;
import br.com.microlog.email.services.CodeRecoveryEmailService;
import br.com.microlog.email.services.NewUserEmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class EmailConsumer {
  final NewUserEmailService newUserEmailService;
  final CodeRecoveryEmailService codeRecoveryEmailService;

  public EmailConsumer(NewUserEmailService newUserEmailService, CodeRecoveryEmailService codeRecoveryEmailService) {
    this.newUserEmailService = newUserEmailService;
    this.codeRecoveryEmailService = codeRecoveryEmailService;
  }

  @RabbitListener(queues = "${broker.queue.new.user.email.name}")
  public void listenNewUserEmailQueue(@Payload EmailRecordDto emailRecordDto) {
    var emailModel = new NewUserEmailModel();
    BeanUtils.copyProperties(emailRecordDto, emailModel);

    newUserEmailService.sendNewUserEmail(emailModel);
  }

  @RabbitListener(queues = "${broker.queue.code.recovery.email.name}")
  public void listenChangeUserPasswordEmailQueue(@Payload CodeRecoveryEmailRecordDto codeRecoveryEmailRecordDto) {
    var emailModel = new CodeRecoveryEmailModel();
    BeanUtils.copyProperties(codeRecoveryEmailRecordDto, emailModel);

    codeRecoveryEmailService.sendCodeRecoveryEmail(emailModel);
  }

  @RabbitListener(queues = "${broker.queue.code.recovery.successfully.email.name}")
  public void listenChangeUserPasswordSuccessfullyEmailQueue(@Payload CodeRecoveryEmailRecordDto codeRecoveryEmailRecordDto) {
    var emailModel = new CodeRecoveryEmailModel();
    BeanUtils.copyProperties(codeRecoveryEmailRecordDto, emailModel);

    codeRecoveryEmailService.sendMessageSuccessfullyActionCodeRecoveryEmail(emailModel);
  }
}
