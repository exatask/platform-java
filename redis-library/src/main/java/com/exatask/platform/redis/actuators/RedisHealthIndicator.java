package com.exatask.platform.redis.actuators;

import com.exatask.platform.redis.constants.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Properties;
import java.util.Set;

@Component(RedisService.HEALTH_CHECK_NAME)
public class RedisHealthIndicator implements HealthIndicator {

  @Autowired(required = false)
  private Set<RedisTemplate<String, String>> redisTemplates;

  @Override
  public Health health() {

    if (CollectionUtils.isEmpty(redisTemplates)) {
      return Health.unknown().build();
    }

    Health.Builder databaseHealth = Health.up();
    for (RedisTemplate redisTemplate : redisTemplates) {

      Health entityHealth = dataSourceHealth(redisTemplate);
      if (entityHealth.getStatus() != Status.UP) {
        databaseHealth.down();
      }

      databaseHealth.withDetail(entityHealth.getDetails().get("database").toString(), entityHealth);
    }

    return databaseHealth.build();
  }

  private Health dataSourceHealth(RedisTemplate redisTemplate) {

    JedisConnectionFactory connectionFactory = (JedisConnectionFactory) redisTemplate.getConnectionFactory();
    Properties redisProperties = connectionFactory.getConnection().info();

    Health.Builder entityHealth = Health.up();
    entityHealth.withDetail("version", redisProperties.getProperty("redis_version"));
    entityHealth.withDetail("database", connectionFactory.getDatabase());

    return entityHealth.build();
  }
}
