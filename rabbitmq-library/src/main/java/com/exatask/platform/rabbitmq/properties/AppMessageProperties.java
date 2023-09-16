package com.exatask.platform.rabbitmq.properties;

import lombok.Builder;
import lombok.Data;
import org.springframework.amqp.core.MessageDeliveryMode;

@Data
@Builder(toBuilder = true)
public class AppMessageProperties {

  private String exchange;

  private MessageDeliveryMode deliveryMode;

  private String routingKey;

  private final Integer delay;
}
