package br.com.microlog.email.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
  @Value("${broker.queue.email.name}")
  private String queue;

  @Value("${broker.queue.new.user.email.name}")
  private String newUserQueue;

  @Value("${broker.queue.code.recovery.email.name}")
  private String codeRecoveryPasswordQueue;

  @Value("${broker.queue.code.recovery.successfully.email.name}")
  private String codeRecoveryPasswordSuccessfullyQueue;

  @Bean
  public Queue queue() {
    return new Queue(queue, true);
  }

  @Bean
  public Queue newUserQueue() {
    return new Queue(newUserQueue, true);
  }

  @Bean
  public Queue codeRecoveryPasswordQueue() {
    return new Queue(codeRecoveryPasswordQueue, true);
  }

  @Bean
  public Queue codeRecoveryPasswordSuccessfullyQueue() {
    return new Queue(codeRecoveryPasswordSuccessfullyQueue, true);
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    ObjectMapper objectMapper = new ObjectMapper();
    return new Jackson2JsonMessageConverter(objectMapper);
  }
}
