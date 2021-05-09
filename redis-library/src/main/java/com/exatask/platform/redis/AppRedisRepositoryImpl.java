package com.exatask.platform.redis;

import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.redis.constants.RedisService;
import com.exatask.platform.redis.exceptions.MissingIdentifierException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.repository.support.SimpleKeyValueRepository;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.RedisKeyValueAdapter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.repository.core.EntityInformation;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

public class AppRedisRepositoryImpl<T, ID extends Serializable> extends SimpleKeyValueRepository<T, ID> implements
                                                                                                             AppRedisRepository<T, ID> {

  private static final AppLogger LOGGER = AppLogManager.getLogger(RedisService.LOGGER_NAME);

  private final EntityInformation<T, ID> entityInformation;

  private final KeyValueOperations keyValueOperations;

  private final RedisTemplate<String, String> redisTemplate;

  private final RedisHash redisHash;

  private final ObjectMapper objectMapper;

  @SneakyThrows({NoSuchFieldException.class, IllegalAccessException.class})
  public AppRedisRepositoryImpl(EntityInformation<T, ID> entityInformation, KeyValueOperations keyValueOperations) {

    super(entityInformation, keyValueOperations);
    this.entityInformation = entityInformation;
    this.keyValueOperations = keyValueOperations;
    this.objectMapper = new ObjectMapper();

    Field adapter = keyValueOperations.getClass().getDeclaredField("adapter");
    adapter.setAccessible(true);
    RedisKeyValueAdapter redisKeyValueAdapter = (RedisKeyValueAdapter) adapter.get(keyValueOperations);
    adapter.setAccessible(false);

    Field redisOps = redisKeyValueAdapter.getClass().getDeclaredField("redisOps");
    redisOps.setAccessible(true);
    this.redisTemplate = (RedisTemplate<String, String>) redisOps.get(redisKeyValueAdapter);
    redisOps.setAccessible(false);

    this.redisHash = entityInformation.getJavaType().getAnnotation(RedisHash.class);
  }

  private String getKey(ID key) {
    return getKey(key.toString());
  }

  private String getKey(String key) {
    return redisHash.value().concat(":").concat(key);
  }

  @Override
  public void set(T value) {
    set(value, redisHash.timeToLive());
  }

  @Override
  public void set(T value, Long ttl) {

    if (ttl <= 0) {
      ttl = 60L;
    }

    ID id = entityInformation.getId(value);
    if (id == null) {
      throw new MissingIdentifierException();
    }

    String data;
    try {
      data = objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException exception) {
      data = value.toString();
    }

    redisTemplate.opsForValue().set(getKey(id), data, ttl, TimeUnit.SECONDS);
  }

  @Override
  public T get(ID key) {

    String data = redisTemplate.opsForValue().get(getKey(key));
    if (data == null || data.length() == 0) {
      return null;
    }

    T value;
    try {
      value = objectMapper.readValue(data, entityInformation.getJavaType());
    } catch (JsonProcessingException exception) {
      value = null;
    }

    return value;
  }

  @Override
  public Boolean delete(ID key) {
    return redisTemplate.delete(getKey(key));
  }
}
