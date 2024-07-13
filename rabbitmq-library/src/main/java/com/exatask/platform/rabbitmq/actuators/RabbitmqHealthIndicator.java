package com.exatask.platform.rabbitmq.actuators;

import com.exatask.platform.rabbitmq.constants.RabbitmqService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;

@Component(RabbitmqService.HEALTH_CHECK_NAME)
public class RabbitmqHealthIndicator implements HealthIndicator {

  @Autowired(required = false)
  private Set<RabbitTemplate> rabbitTemplates;

  @Override
  public Health health() {

    if (CollectionUtils.isEmpty(rabbitTemplates)) {
      return Health.unknown().build();
    }

    Health.Builder rabbitmqHealth = Health.up();
    for (RabbitTemplate rabbitTemplate : rabbitTemplates) {

      Health entityHealth = rabbitmqHealth(rabbitTemplate);
      if (entityHealth.getStatus() != Status.UP) {
        rabbitmqHealth.down();
      }

      rabbitmqHealth.withDetail(entityHealth.getDetails().get("name").toString(), entityHealth);
    }

    return rabbitmqHealth.build();
  }

  private Health rabbitmqHealth(RabbitTemplate rabbitTemplate) {

    Health.Builder entityHealth = Health.up();

    Map<String, Object> serverProperties = rabbitTemplate.execute(channel -> channel.getConnection().getServerProperties());
    Map<String, Object> clientProperties = rabbitTemplate.execute(channel -> channel.getConnection().getClientProperties());

    entityHealth.withDetail("version", serverProperties.get("version").toString());
    entityHealth.withDetail("name", clientProperties.get("connection_name").toString());

    return entityHealth.build();
  }
}
