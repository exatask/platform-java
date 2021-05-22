package com.exatask.platform.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppRedisTemplate {

  public StringRedisTemplate getRedisTemplate(RedisProperties redisProperties) {

    RedisConnectionFactory redisConnection = getRedisConnectionFactory(redisProperties);
    StringRedisSerializer stringSerializer = getStringRedisSerializer();
    GenericJackson2JsonRedisSerializer jsonSerializer = getJsonRedisSerializer();

    StringRedisTemplate redisTemplate = new StringRedisTemplate();
    redisTemplate.setConnectionFactory(redisConnection);
    redisTemplate.setEnableTransactionSupport(false);

    redisTemplate.setEnableDefaultSerializer(false);
    redisTemplate.setStringSerializer(stringSerializer);
    redisTemplate.setKeySerializer(stringSerializer);
    redisTemplate.setValueSerializer(jsonSerializer);
    redisTemplate.setHashKeySerializer(stringSerializer);
    redisTemplate.setHashValueSerializer(jsonSerializer);

    return redisTemplate;
  }

  private RedisConnectionFactory getRedisConnectionFactory(RedisProperties redisProperties) {

    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
    redisStandaloneConfiguration.setDatabase(redisProperties.getDatabase());
    Optional.ofNullable(redisProperties.getPassword()).ifPresent(redisStandaloneConfiguration::setPassword);

    return new JedisConnectionFactory(redisStandaloneConfiguration);
  }

  private StringRedisSerializer getStringRedisSerializer() {
    return new StringRedisSerializer();
  }

  private GenericJackson2JsonRedisSerializer getJsonRedisSerializer() {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    return new GenericJackson2JsonRedisSerializer(objectMapper);
  }
}
