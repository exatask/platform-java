package com.exatask.platform.dao.migration.libraries;

import com.exatask.platform.dao.libraries.AppLibrary;
import com.exatask.platform.utilities.ServiceUtility;
import com.exatask.platform.utilities.services.ServiceAuthData;
import io.mongock.runner.core.executor.MongockRunner;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.sql.DataSource;

public class ServiceLibrary extends AppLibrary {

  private static final String CHANGELOG_COLLECTION = "service_changelogs";
  private static final String CHANGELOG_PACKAGE = "service.changelogs.package";

  private final MongodbLibrary mongodbLibrary = new MongodbLibrary();
  private final MysqlLibrary mysqlLibrary = new MysqlLibrary();

  public MongockRunner createRunner(MongoProperties mongoProperties) {
    return mongodbLibrary.createRunner(mongoProperties, ServiceUtility.getServiceProperty(CHANGELOG_PACKAGE), CHANGELOG_COLLECTION);
  }

  public MongockRunner createRunner(MongoTemplate mongoTemplate) {
    return mongodbLibrary.createRunner(mongoTemplate, ServiceUtility.getServiceProperty(CHANGELOG_PACKAGE), CHANGELOG_COLLECTION);
  }

  public Flyway createRunner(DataSourceProperties dataSourceProperties) {
    return mysqlLibrary.createRunner(dataSourceProperties, ServiceUtility.getServiceProperty(CHANGELOG_PACKAGE), CHANGELOG_COLLECTION);
  }

  public Flyway createRunner(DataSource dataSource) {
    return mysqlLibrary.createRunner(dataSource, ServiceUtility.getServiceProperty(CHANGELOG_PACKAGE), CHANGELOG_COLLECTION);
  }

  public WebClient createWebClient(String authType, String authToken) {

    return WebClient.builder()
        .baseUrl("http://localhost:" + ServiceUtility.getServiceProperty("server.port"))
        .defaultHeader(ServiceAuthData.AUTH_TYPE_HEADER, authType)
        .defaultHeader(ServiceAuthData.AUTH_TOKEN_HEADER, authToken)
        .build();
  }
}
