package com.exatask.platform.rabbitmq.utilities;

import com.exatask.platform.rabbitmq.AppListener;
import lombok.experimental.UtilityClass;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;

@UtilityClass
public class TemplateUtility {

  public static SimpleMessageListenerContainer getMessageListener(ConnectionFactory connectionFactory, String queueName, AppListener listener) {

    Queue queue = new Queue(queueName);

    SimpleMessageListenerContainer messageListenerContainer = new SimpleMessageListenerContainer();
    messageListenerContainer.setConnectionFactory(connectionFactory);
    messageListenerContainer.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    messageListenerContainer.addQueues(queue);
    messageListenerContainer.setMessageListener(listener);

    return messageListenerContainer;
  }

  public static RabbitTemplate getTemplate(ConnectionFactory connectionFactory, Binding binding) {

    Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();

    RabbitTemplate rabbitTemplate = new RabbitTemplate();
    rabbitTemplate.setConnectionFactory(connectionFactory);
    rabbitTemplate.setMessageConverter(jsonConverter);
    rabbitTemplate.setExchange(binding.getExchange());
    rabbitTemplate.setRoutingKey(binding.getRoutingKey());

    return rabbitTemplate;
  }

  public static ConnectionFactory getConnectionFactory(RabbitProperties properties) {

    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    connectionFactory.setHost(properties.getHost());
    connectionFactory.setPort(properties.getPort());
    connectionFactory.setUsername(properties.getUsername());
    connectionFactory.setPassword(properties.getPassword());
    connectionFactory.setVirtualHost(properties.getVirtualHost());

    return connectionFactory;
  }
}
