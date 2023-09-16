package com.exatask.platform.rabbitmq.utilities;

import com.exatask.platform.rabbitmq.listeners.AppListener;
import com.exatask.platform.utilities.ApplicationContextUtility;
import lombok.experimental.UtilityClass;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpoint;

@UtilityClass
public class ListenerUtility {

  public static RabbitListenerContainerFactory getListenerContainer(ConnectionFactory connectionFactory) {

    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
    rabbitListenerContainerFactory.setConnectionFactory(connectionFactory);
    rabbitListenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    rabbitListenerContainerFactory.setPrefetchCount(1);
    rabbitListenerContainerFactory.setApplicationContext(ApplicationContextUtility.getApplicationContext());
    rabbitListenerContainerFactory.setAutoStartup(false);

    return rabbitListenerContainerFactory;
  }

  public static RabbitListenerEndpoint getListenerEndpoint(AppListener listener, String... queues) {

    SimpleRabbitListenerEndpoint listenerEndpoint = new SimpleRabbitListenerEndpoint();
    listenerEndpoint.setMessageListener(listener);
    listenerEndpoint.setQueueNames(queues);
    listenerEndpoint.setAutoStartup(true);

    return listenerEndpoint;
  }
}
