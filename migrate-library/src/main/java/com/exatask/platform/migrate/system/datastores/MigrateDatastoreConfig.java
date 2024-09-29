package com.exatask.platform.migrate.system.datastores;

import com.exatask.platform.utilities.properties.DataSourceSqlProperties;
import com.exatask.platform.utilities.properties.MongodbProperties;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "datastores")
public class MigrateDatastoreConfig {

  private final HashMap<String, DataSourceSqlProperties> mysql = new HashMap<>();

  private final HashMap<String, DataSourceSqlProperties> oracle = new HashMap<>();

  private final HashMap<String, DataSourceSqlProperties> postgresql = new HashMap<>();

  private final HashMap<String, DataSourceSqlProperties> mariadb = new HashMap<>();

  private final HashMap<String, MongodbProperties> mongodb = new HashMap<>();
}
