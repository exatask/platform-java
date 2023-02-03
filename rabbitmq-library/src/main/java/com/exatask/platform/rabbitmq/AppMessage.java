package com.exatask.platform.rabbitmq;

import lombok.Data;
import org.springframework.amqp.core.MessageDeliveryMode;

@Data
public abstract class AppMessage {

  private String routingKey;

  public abstract MessageDeliveryMode deliveryMode();
}
