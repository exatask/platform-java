package com.exatask.platform.rabbitmq;

import org.springframework.amqp.core.MessageDeliveryMode;

public interface AppMessage {

  String routingKey();

  MessageDeliveryMode deliveryMode();
}
