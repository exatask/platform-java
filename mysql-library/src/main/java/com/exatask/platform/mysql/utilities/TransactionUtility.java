package com.exatask.platform.mysql.utilities;

import com.exatask.platform.mysql.AppMysqlTenantConnectionProvider;
import com.exatask.platform.mysql.tenants.MysqlTenantConnections;
import com.exatask.platform.mysql.tenants.MysqlTenantResolver;
import com.exatask.platform.mysql.tenants.ServiceTenant;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.MySQL8Dialect;
import org.hibernate.tool.schema.Action;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class TransactionUtility {

  public static PlatformTransactionManager getTransactionManager(EntityManagerFactory entityManagerFactory) {

    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    return transactionManager;
  }

  public static LocalContainerEntityManagerFactoryBean getEntityManagerFactory(DataSource dataSource, String[] packages) {

    LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
    entityManager.setDataSource(dataSource);
    entityManager.setPackagesToScan(packages);
    entityManager.setJpaVendorAdapter(getVendorAdapter());
    entityManager.setJpaPropertyMap(prepareJpaProperties());

    return entityManager;
  }

  public static LocalContainerEntityManagerFactoryBean getEntityManagerFactory(ServiceTenant serviceTenant, String[] packages) {

    MysqlTenantConnections mysqlTenantConnections = new MysqlTenantConnections(serviceTenant);
    MysqlTenantResolver mysqlTenantResolver = new MysqlTenantResolver(serviceTenant);
    AppMysqlTenantConnectionProvider mysqlTenantConnectionProvider = new AppMysqlTenantConnectionProvider(mysqlTenantConnections);

    Map<String, Object> jpaProperties = prepareJpaProperties();
    jpaProperties.put(AvailableSettings.MULTI_TENANT, MultiTenancyStrategy.DATABASE.toString());
    jpaProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, mysqlTenantConnectionProvider);
    jpaProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, mysqlTenantResolver);

    LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
    entityManager.setDataSource(mysqlTenantConnections.getTenantDataSource());
    entityManager.setPackagesToScan(packages);
    entityManager.setJpaVendorAdapter(getVendorAdapter());
    entityManager.setJpaPropertyMap(jpaProperties);

    return entityManager;
  }

  public static DataSource getDataSource(DataSourceProperties properties) {

    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setJdbcUrl(properties.getUrl());
    hikariDataSource.setUsername(properties.getUsername());
    hikariDataSource.setPassword(properties.getPassword());
    hikariDataSource.addDataSourceProperty("createDatabaseIfNotExist", true);

    return hikariDataSource;
  }

  private static HibernateJpaVendorAdapter getVendorAdapter() {

    return new HibernateJpaVendorAdapter();
  }

  private static Map<String, Object> prepareJpaProperties() {

    Map<String, Object> jpaProperties = new HashMap<>();
    jpaProperties.put(AvailableSettings.HBM2DDL_AUTO, Action.NONE.name().toLowerCase());
    jpaProperties.put(AvailableSettings.DIALECT, MySQL8Dialect.class);
    jpaProperties.put(AvailableSettings.JDBC_TIME_ZONE, ZoneOffset.UTC.toString());
    return jpaProperties;
  }
}
