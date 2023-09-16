package com.exatask.platform.rabbitmq.publishers;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.rabbitmq.utilities.TemplateUtility;
import com.exatask.platform.utilities.properties.RabbitmqProperties;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

@Service
public class PublisherClientTemplate {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  public <T extends AppPublisher> T getServicePublisher(Class<T> clazz, RabbitmqProperties rabbitmqProperties) {

    try {

      ConnectionFactory connectionFactory = TemplateUtility.getConnectionFactory(rabbitmqProperties);
      RabbitTemplate rabbitTemplate = TemplateUtility.getTemplate(connectionFactory);
      return clazz.getConstructor(RabbitTemplate.class).newInstance(rabbitTemplate);

    } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException exception) {

      LOGGER.error(exception);
      return null;
    }
  }
}
