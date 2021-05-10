package com.exatask.platform.redis.healthcheck;

import com.exatask.platform.redis.constants.RedisService;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheck;
import com.exatask.platform.utilities.healthcheck.ServiceHealthCheckData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

@Component
public class RedisHealthCheck implements ServiceHealthCheck {

  @Autowired(required = false)
  private Set<RedisTemplate<String, String>> redisTemplates;

  public Set<ServiceHealthCheckData> healthCheck() {

    Set<ServiceHealthCheckData> redisHealthCheckData = new HashSet<>();
    if (CollectionUtils.isEmpty(redisTemplates)) {
      return redisHealthCheckData;
    }

    for (RedisTemplate<String, String> template : redisTemplates) {

      RedisConnection redisConnection = RedisConnectionUtils.getConnection(template.getConnectionFactory());
      Properties redisProperties = redisConnection.info();

      redisHealthCheckData.add(ServiceHealthCheckData.builder()
          .status(true)
          .version(redisProperties.getProperty("redis_version"))
          .uptime(redisProperties.getProperty("uptime_in_seconds"))
          .build());
    }

    return redisHealthCheckData;
  }

  @Override
  public String getName() {
    return RedisService.HEALTH_CHECK_NAME;
  }
}
