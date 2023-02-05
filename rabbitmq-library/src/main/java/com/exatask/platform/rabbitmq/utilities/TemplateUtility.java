package com.exatask.platform.rabbitmq.utilities;

import com.exatask.platform.rabbitmq.exceptions.InvalidExchangeTypeException;
import com.exatask.platform.utilities.ApplicationContextUtility;
import com.exatask.platform.utilities.properties.RabbitmqProperties;
import lombok.experimental.UtilityClass;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class TemplateUtility {

  public static RabbitListenerContainerFactory getListenerContainer(ConnectionFactory connectionFactory) {

    SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory = new SimpleRabbitListenerContainerFactory();
    rabbitListenerContainerFactory.setConnectionFactory(connectionFactory);
    rabbitListenerContainerFactory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
    rabbitListenerContainerFactory.setPrefetchCount(1);
    rabbitListenerContainerFactory.setApplicationContext(ApplicationContextUtility.getApplicationContext());
    rabbitListenerContainerFactory.setAutoStartup(false);

    return rabbitListenerContainerFactory;
  }

  public static RabbitTemplate getTemplate(ConnectionFactory connectionFactory) {

    Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();

    RabbitTemplate rabbitTemplate = new RabbitTemplate();
    rabbitTemplate.setConnectionFactory(connectionFactory);
    rabbitTemplate.setMessageConverter(jsonConverter);

    return rabbitTemplate;
  }

  public static ConnectionFactory getConnectionFactory(RabbitmqProperties properties) {

    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    connectionFactory.setHost(properties.getHost());
    connectionFactory.setPort(properties.getPort());
    connectionFactory.setUsername(properties.getUsername());
    connectionFactory.setPassword(properties.getPassword());
    connectionFactory.setVirtualHost(properties.getVirtualHost());

    Optional.ofNullable(properties.getMaximum()).ifPresent(connectionFactory::setConnectionLimit);
    Optional.ofNullable(properties.getConnectionTimeout()).ifPresent(time -> connectionFactory.setConnectionTimeout((int) time.toMillis()));

    return connectionFactory;
  }

  public static Declarables getDeclarables(Map<RabbitmqProperties.Exchange, RabbitmqProperties.Queue> bindings) {

    List<Declarable> declarables = new ArrayList<>();
    for (Map.Entry<RabbitmqProperties.Exchange, RabbitmqProperties.Queue> binding : bindings.entrySet()) {

      Exchange exchange = initializeExchange(binding.getKey());
      declarables.add(exchange);

      Queue queue = initializeQueue(binding.getValue());
      declarables.add(queue);

      declarables.add(BindingBuilder.bind(queue)
          .to(exchange)
          .with(binding.getValue().getRoutingKey())
          .noargs());
    }

    return new Declarables(declarables);
  }

  private Exchange initializeExchange(RabbitmqProperties.Exchange exchange) {

    Boolean durable = Boolean.TRUE.equals(exchange.getDurable());
    Boolean autoDelete = Boolean.TRUE.equals(exchange.getAutoDelete());

    switch (exchange.getType()) {

      case DIRECT:
        return new DirectExchange(exchange.getName(), durable, autoDelete);

      case TOPIC:
        return new TopicExchange(exchange.getName(), durable, autoDelete);

      case FANOUT:
        return new FanoutExchange(exchange.getName(), durable, autoDelete);

      case HEADERS:
        return new HeadersExchange(exchange.getName(), durable, autoDelete);

      default:
        throw new InvalidExchangeTypeException(exchange.getType().toString());
    }
  }

  private Queue initializeQueue(RabbitmqProperties.Queue queue) {
    return new Queue(queue.getName(), Boolean.TRUE.equals(queue.getDurable()), Boolean.TRUE.equals(queue.getExclusive()), Boolean.TRUE.equals(queue.getAutoDelete()));
  }
}
