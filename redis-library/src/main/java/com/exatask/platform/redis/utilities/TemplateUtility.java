package com.exatask.platform.redis.utilities;

import lombok.experimental.UtilityClass;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Optional;

@UtilityClass
public class TemplateUtility {

  public static StringRedisTemplate getTemplate(RedisConnectionFactory connectionFactory) {

    StringRedisSerializer stringSerializer = new StringRedisSerializer();
    Jackson2JsonRedisSerializer<String> jsonSerializer = new Jackson2JsonRedisSerializer<>(String.class);

    StringRedisTemplate redisTemplate = new StringRedisTemplate();
    redisTemplate.setConnectionFactory(connectionFactory);
    redisTemplate.setEnableTransactionSupport(false);

    redisTemplate.setEnableDefaultSerializer(false);
    redisTemplate.setStringSerializer(stringSerializer);
    redisTemplate.setKeySerializer(stringSerializer);
    redisTemplate.setValueSerializer(jsonSerializer);
    redisTemplate.setHashKeySerializer(stringSerializer);
    redisTemplate.setHashValueSerializer(jsonSerializer);

    return redisTemplate;
  }

  public static RedisConnectionFactory getConnectionFactory(RedisProperties redisProperties) {

    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
    redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
    Optional.ofNullable(redisProperties.getUsername()).ifPresent(redisStandaloneConfiguration::setUsername);
    Optional.ofNullable(redisProperties.getPassword()).ifPresent(redisStandaloneConfiguration::setPassword);

    return new JedisConnectionFactory(redisStandaloneConfiguration);
  }
}
