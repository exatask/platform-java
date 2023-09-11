package com.exatask.platform.rabbitmq.utilities;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.rabbitmq.exceptions.InvalidExchangeTypeException;
import com.exatask.platform.utilities.constants.RabbitmqConstant;
import com.exatask.platform.utilities.properties.RabbitmqProperties;
import com.exatask.platform.utilities.services.ServiceName;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import lombok.experimental.UtilityClass;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.CustomExchange;
import org.springframework.amqp.core.Declarable;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@UtilityClass
public class TemplateUtility {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

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

  public static Declarables getDeclarables(ServiceName service, List<RabbitmqProperties.Binding> bindings, ConnectionFactory connectionFactory) {

    Channel channel = connectionFactory.createConnection().createChannel(false);

    List<Declarable> declarables = new ArrayList<>();
    for (RabbitmqProperties.Binding binding : bindings) {

      Exchange exchange = initializeExchange(service, binding.getExchange(), channel);
      declarables.add(exchange);

      Queue queue = initializeQueue(service, binding.getQueue(), channel);
      declarables.add(queue);

      Binding bindingDetails = initializeBinding(exchange, queue, binding.getRoutingKey(), channel);
      declarables.add(bindingDetails);
    }

    try {
      channel.close();
    } catch (IOException | TimeoutException exception) {
      LOGGER.error(exception);
    }

    return new Declarables(declarables);
  }

  public String getExchangeName(ServiceName service, String exchange) {
    return service.name().toLowerCase() + "." + exchange;
  }

  public String getQueueName(ServiceName service, String queue) {
    return service.name().toLowerCase() + "." + queue;
  }

  private Exchange initializeExchange(ServiceName service, RabbitmqProperties.Exchange exchange, Channel channel) {

    String exchangeName = getExchangeName(service, exchange.getName());
    BuiltinExchangeType exchangeType = getExchangeType(exchange.getType());
    boolean durable = Boolean.TRUE.equals(exchange.getDurable());
    boolean autoDelete = Boolean.TRUE.equals(exchange.getAutoDelete());

    try {
      channel.exchangeDeclare(exchangeName, exchangeType, durable, autoDelete, null);
    } catch (IOException exception) {
      LOGGER.error(exception);
    }

    switch (exchange.getType()) {

      case DIRECT:
        return new DirectExchange(exchangeName, durable, autoDelete);

      case TOPIC:
        return new TopicExchange(exchangeName, durable, autoDelete);

      case FANOUT:
        return new FanoutExchange(exchangeName, durable, autoDelete);

      case HEADERS:
        return new HeadersExchange(exchangeName, durable, autoDelete);

      case DELAYED:
        Map<String, Object> exchangeArguments = Collections.singletonMap("x-delayed-type", "direct");
        return new CustomExchange(exchangeName, "x-delayed-message", durable, autoDelete, exchangeArguments);

      default:
        throw new InvalidExchangeTypeException(exchange.getType().toString());
    }
  }

  private Queue initializeQueue(ServiceName service, RabbitmqProperties.Queue queue, Channel channel) {

    String queueName = getQueueName(service, queue.getName());
    boolean durable = Boolean.TRUE.equals(queue.getDurable());
    boolean exclusive = Boolean.TRUE.equals(queue.getExclusive());
    boolean autoDelete = Boolean.TRUE.equals(queue.getAutoDelete());

    try {
      channel.queueDeclare(queueName, durable, exclusive, autoDelete, null);
    } catch (IOException exception) {
      LOGGER.error(exception);
    }

    return new Queue(queueName, durable, exclusive, autoDelete);
  }

  private Binding initializeBinding(Exchange exchange, Queue queue, String routingKey, Channel channel) {

    try {
      channel.queueBind(queue.getName(), exchange.getName(), routingKey);
    } catch (IOException exception) {
      LOGGER.error(exception);
    }

    return BindingBuilder.bind(queue)
        .to(exchange)
        .with(routingKey)
        .noargs();
  }

  private BuiltinExchangeType getExchangeType(RabbitmqConstant.Type type) {

    switch (type) {

      case DIRECT:
      case DELAYED:
        return BuiltinExchangeType.DIRECT;

      case TOPIC:
        return BuiltinExchangeType.TOPIC;

      case FANOUT:
        return BuiltinExchangeType.FANOUT;

      case HEADERS:
        return BuiltinExchangeType.HEADERS;
    }

    return BuiltinExchangeType.DIRECT;
  }
}
