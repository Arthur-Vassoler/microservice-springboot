package br.com.microlog.user.producers;

import br.com.microlog.user.dtos.CodeRecoveryEmailDto;
import br.com.microlog.user.dtos.EmailDto;
import br.com.microlog.user.enums.TypeCodeRecoveryEnum;
import br.com.microlog.user.models.UserModel;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class UserProducer {
  final RabbitTemplate rabbitTemplate;

  public UserProducer(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }

  @Value(value = "${broker.queue.new.user.email.name}")
  private String saveUserRoutingKey;

  @Value(value = "${broker.queue.code.recovery.email.name}")
  private String codeRecoveryRoutingKey;

  @Value(value = "${broker.queue.code.recovery.successfully.email.name}")
  private String codeRecoverySuccessfullyRoutingKey;

  public void publishCodeRecoveryMessageEmail(UserModel userModel, String token, TypeCodeRecoveryEnum typeCodeRecoveryEnum) {
    var emailDto = new CodeRecoveryEmailDto();
    emailDto.setUserId(userModel.getUserId());
    emailDto.setUsername(userModel.getName());
    emailDto.setEmailTo(userModel.getEmail());
    emailDto.setToken(token);
    emailDto.setTypeCodeRecovery(typeCodeRecoveryEnum);

    rabbitTemplate.convertAndSend("", codeRecoveryRoutingKey, emailDto);
  }

  public void publishNewUserMessageEmail(UserModel userModel) {
    var emailDto = new EmailDto();
    emailDto.setUserId(userModel.getUserId());
    emailDto.setUsername(userModel.getName());
    emailDto.setEmailTo(userModel.getEmail());

    rabbitTemplate.convertAndSend("", saveUserRoutingKey, emailDto);
  }

  public void publishCodeRecoverySuccessfullyMessageEmail(UserModel userModel, TypeCodeRecoveryEnum typeCodeRecovery) {
    var emailDto = new CodeRecoveryEmailDto();
    emailDto.setUserId(userModel.getUserId());
    emailDto.setEmailTo(userModel.getEmail());
    emailDto.setTypeCodeRecovery(typeCodeRecovery);

    rabbitTemplate.convertAndSend("", codeRecoverySuccessfullyRoutingKey, emailDto);
  }
}
