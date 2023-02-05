package com.exatask.platform.rabbitmq.utilities;

import com.exatask.platform.rabbitmq.AppListener;
import lombok.experimental.UtilityClass;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpoint;

@UtilityClass
public class ListenerUtility {

    public static RabbitListenerEndpoint getListenerEndpoint(AppListener listener, String... queues) {

        SimpleRabbitListenerEndpoint listenerEndpoint = new SimpleRabbitListenerEndpoint();
        listenerEndpoint.setMessageListener(listener);
        listenerEndpoint.setQueueNames(queues);
        listenerEndpoint.setAutoStartup(true);

        return listenerEndpoint;
    }
}
