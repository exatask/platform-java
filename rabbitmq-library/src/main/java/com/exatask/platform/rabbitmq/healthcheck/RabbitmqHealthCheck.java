package com.exatask.platform.rabbitmq.healthcheck;

import com.exatask.platform.rabbitmq.constants.RabbitmqService;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheck;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheckData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class RabbitmqHealthCheck implements ServiceHealthCheck {

  @Autowired(required = false)
  private Set<RabbitTemplate> rabbitTemplates;

  public Set<ServiceHealthCheckData> healthCheck() {

    Set<ServiceHealthCheckData> rabbitmqHealthCheckData = new HashSet<>();
    if (CollectionUtils.isEmpty(rabbitTemplates)) {
      return rabbitmqHealthCheckData;
    }

    for (RabbitTemplate rabbitTemplate : rabbitTemplates) {

      Map<String,Object> serverProperties = rabbitTemplate.execute(channel -> channel.getConnection().getServerProperties());

      rabbitmqHealthCheckData.add(RabbitmqHealthCheckData.builder()
          .status(true)
          .version(serverProperties.get("version").toString())
          .cluster(serverProperties.get("cluster_name").toString())
          .build());
    }

    return rabbitmqHealthCheckData;
  }

  @Override
  public String getName() {
    return RabbitmqService.HEALTH_CHECK_NAME;
  }
}
