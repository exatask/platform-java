package com.exatask.platform.rabbitmq.utilities;

import com.exatask.platform.rabbitmq.exceptions.InvalidExchangeTypeException;
import com.exatask.platform.utilities.constants.RabbitmqConstant;
import lombok.experimental.UtilityClass;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.HeadersExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;

@UtilityClass
public class ExchangeUtility {

  public Exchange getExchange(RabbitmqConstant.Type type, String exchange) {

    switch (type) {

      case DIRECT:
        return new DirectExchange(exchange);

      case TOPIC:
        return new TopicExchange(exchange);

      case FANOUT:
        return new FanoutExchange(exchange);

      case HEADERS:
        return new HeadersExchange(exchange);

      default:
        throw new InvalidExchangeTypeException(type.toString());
    }
  }

  public Binding getBinding(Exchange exchange, String queueName, String routingKey) {

    return BindingBuilder.bind(new Queue(queueName))
        .to(exchange)
        .with(routingKey)
        .noargs();
  }

  public Binding getBinding(Exchange exchange, Exchange bindExchange, String routingKey) {

    return BindingBuilder.bind(bindExchange)
        .to(exchange)
        .with(routingKey)
        .noargs();
  }
}
