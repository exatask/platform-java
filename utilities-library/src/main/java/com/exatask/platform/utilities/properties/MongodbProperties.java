package com.exatask.platform.utilities.properties;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class MongodbProperties extends MongoProperties {

  private Integer minimum;

  private Integer maximum;

  private Integer timeout;

  private Integer idleTimeout;

  private List<SecondaryHost> secondaryHosts;

  @Data
  @Accessors(chain = true)
  public static class SecondaryHost {

    private String host;

    private Integer port;
  }
}
