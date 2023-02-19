package com.exatask.platform.utilities.properties;

import com.exatask.platform.utilities.constants.RabbitmqConstant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class RabbitmqProperties extends RabbitProperties {

  private Integer maximum;

  private List<Binding> bindings;

  @Data
  @Accessors(chain = true)
  public static class Binding {

    private Exchange exchange;

    private Queue queue;

    private String routingKey;
  }

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
  }
}
