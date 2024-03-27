package com.exatask.platform.mongodb.utilities;

import com.exatask.platform.utilities.properties.MongodbProperties;
import com.mongodb.ConnectionString;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class ConnectionUtility {

  private static final String MONGODB_PREFIX = "mongodb://";

  private static final String DEFAULT_HOST = "localhost";
  private static final int DEFAULT_PORT = 27017;

  public static ConnectionString prepareConnectionString(MongodbProperties mongoProperties) {

    String mongoUri = mongoProperties.getUri();
    if (StringUtils.isEmpty(mongoUri)) {
      mongoUri = prepareMongoUri(mongoProperties);
    }

    return new ConnectionString(mongoUri);
  }

  public static String prepareMongoUri(MongodbProperties mongoProperties) {

    StringBuilder mongoUriBuilder = new StringBuilder(MONGODB_PREFIX);

    Optional.ofNullable(mongoProperties.getUsername()).ifPresent(username -> mongoUriBuilder.append(username)
        .append(":")
        .append(mongoProperties.getPassword())
        .append("@"));

    mongoUriBuilder.append(Optional.ofNullable(mongoProperties.getHost()).orElse(DEFAULT_HOST))
        .append(":")
        .append(Optional.ofNullable(mongoProperties.getPort()).orElse(DEFAULT_PORT));

    if (!CollectionUtils.isEmpty(mongoProperties.getSecondaryHosts())) {

      mongoUriBuilder.append(mongoProperties.getSecondaryHosts().stream()
          .map(host -> Optional.ofNullable(host.getHost()).orElse(DEFAULT_HOST) + ":" +
              Optional.ofNullable(host.getPort()).orElse(DEFAULT_PORT))
          .collect(Collectors.joining(",")));
    }

    mongoUriBuilder.append("/")
        .append(mongoProperties.getDatabase())
        .append("?");

    List<String> properties = new ArrayList<>();

    Optional.ofNullable(mongoProperties.getAuthenticationDatabase())
        .ifPresent(database -> properties.add("authSource=" + database));

    Optional.ofNullable(mongoProperties.getReplicaSetName())
        .ifPresent(replicaSet -> properties.add("replicaSet=" + replicaSet));

    Optional.ofNullable(mongoProperties.getMinimum())
        .ifPresent(minimum -> properties.add("minPoolSize=" + minimum));

    Optional.ofNullable(mongoProperties.getMaximum())
        .ifPresent(maximum -> properties.add("maxPoolSize=" + maximum));

    Optional.ofNullable(mongoProperties.getTimeout())
        .ifPresent(timeout -> properties.add("connectTimeoutMS=" + timeout));

    Optional.ofNullable(mongoProperties.getIdleTimeout())
        .ifPresent(idleTimeout -> properties.add("maxIdleTimeMS=" + idleTimeout));

    Optional.ofNullable(mongoProperties.getWriteConcern())
        .ifPresent(writeConcern -> properties.add("w=" + writeConcern));

    Optional.of(mongoProperties.isJournal())
        .ifPresent(journal -> properties.add("journal=" + journal));

    Optional.ofNullable(mongoProperties.getReadPreference())
        .ifPresent(readPreference -> properties.add("readPreference=" + readPreference.getValue()));

    mongoUriBuilder.append(String.join("&", properties));

    return mongoUriBuilder.toString();
  }
}
