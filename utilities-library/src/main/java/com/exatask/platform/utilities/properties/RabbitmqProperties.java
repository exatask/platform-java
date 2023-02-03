package com.exatask.platform.utilities.properties;

import com.exatask.platform.utilities.constants.RabbitmqConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
public class RabbitmqProperties extends RabbitProperties {

  private Integer maximum;

  private Map<Exchange, Queue> bindings;

  @Data
  @Accessors(chain = true)
  public static class Exchange {

    private RabbitmqConstant.Type type;

    private String name;

    private Boolean durable;

    private Boolean autoDelete;
  }

  @Data
  @Accessors(chain = true)
  public static class Queue {

    private String name;

    private Boolean durable;

    private Boolean exclusive;

    private Boolean autoDelete;

    private String routingKey;
  }
}
