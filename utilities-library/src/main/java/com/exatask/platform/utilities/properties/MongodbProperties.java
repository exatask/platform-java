package com.exatask.platform.utilities.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;

@Data
@EqualsAndHashCode(callSuper = true)
public class MongodbProperties extends MongoProperties {

  private Integer minimum;

  private Integer maximum;

  private Integer timeout;

  private Integer idleTimeout;
}
