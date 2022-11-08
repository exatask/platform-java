package com.exatask.platform.rabbitmq;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;

import java.io.IOException;

public abstract class AppListener<T extends AppMessage> implements ChannelAwareMessageListener {

  protected static final AppLogger LOGGER = AppLogManager.getLogger();

  protected final ObjectMapper objectMapper = new ObjectMapper();

  public abstract boolean execute(T message);

  public abstract Class<T> getType();

  private void acknowledge(Channel channel, long deliveryTag, boolean acknowledge) {

    try {

      if (acknowledge) {
        channel.basicAck(deliveryTag, false);
      } else {
        channel.basicNack(deliveryTag, false, true);
      }

    } catch (IOException exception) {
      LOGGER.error(exception);
    }
  }

  @Override
  public void onMessage(Message message, Channel channel) {

    String messageBody = new String(message.getBody());
    LOGGER.info(messageBody);

    try {

      T messageObject = objectMapper.readValue(messageBody, getType());
      boolean result = this.execute(messageObject);
      acknowledge(channel, message.getMessageProperties().getDeliveryTag(), result);

    } catch (JsonProcessingException exception) {

      LOGGER.error(exception);
      acknowledge(channel, message.getMessageProperties().getDeliveryTag(), false);
    }
  }
}
