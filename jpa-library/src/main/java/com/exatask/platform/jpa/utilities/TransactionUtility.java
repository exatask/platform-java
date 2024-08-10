package com.exatask.platform.jpa.utilities;

import com.exatask.platform.jpa.AppJpaTenantConnectionProvider;
import com.exatask.platform.jpa.replicas.ReplicaDataSource;
import com.exatask.platform.jpa.replicas.ReplicaTransactionManager;
import com.exatask.platform.jpa.tenants.JpaTenantConnections;
import com.exatask.platform.jpa.tenants.JpaTenantResolver;
import com.exatask.platform.jpa.tenants.ServiceTenant;
import com.exatask.platform.utilities.properties.DataSourceSqlProperties;
import com.zaxxer.hikari.HikariDataSource;
import lombok.experimental.UtilityClass;
import org.hibernate.MultiTenancyStrategy;
import org.hibernate.cfg.AvailableSettings;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class TransactionUtility {

  public static PlatformTransactionManager getTransactionManager(EntityManagerFactory entityManagerFactory) {

    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(entityManagerFactory);
    return new ReplicaTransactionManager(transactionManager);
  }

  public static LocalContainerEntityManagerFactoryBean getEntityManagerFactory(DataSource dataSource, String[] packages, Map<String, Object> jpaProperties) {

    LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
    entityManager.setDataSource(dataSource);
    entityManager.setPackagesToScan(packages);
    entityManager.setJpaVendorAdapter(getVendorAdapter());
    entityManager.setJpaPropertyMap(jpaProperties);

    return entityManager;
  }

  public static LocalContainerEntityManagerFactoryBean getEntityManagerFactory(ServiceTenant serviceTenant, String[] packages, Map<String, Object> jpaProperties) {

    JpaTenantConnections jpaTenantConnections = new JpaTenantConnections(serviceTenant);
    JpaTenantResolver jpaTenantResolver = new JpaTenantResolver(serviceTenant);
    AppJpaTenantConnectionProvider jpaTenantConnectionProvider = new AppJpaTenantConnectionProvider(jpaTenantConnections);

    jpaProperties.put(AvailableSettings.MULTI_TENANT, MultiTenancyStrategy.DATABASE.toString());
    jpaProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, jpaTenantConnectionProvider);
    jpaProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, jpaTenantResolver);

    LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
    entityManager.setDataSource(jpaTenantConnections.getTenantDataSource());
    entityManager.setPackagesToScan(packages);
    entityManager.setJpaVendorAdapter(getVendorAdapter());
    entityManager.setJpaPropertyMap(jpaProperties);

    return entityManager;
  }

  public static DataSource getDataSource(DataSourceSqlProperties dataSourceProperties) {
    return new ReplicaDataSource(dataSourceProperties);
  }

  public static DataSource getDataSource(String url, String username, String password, DataSourceSqlProperties dataSourceProperties) {

    HikariDataSource hikariDataSource = new HikariDataSource();
    hikariDataSource.setJdbcUrl(url);
    hikariDataSource.setUsername(username);
    hikariDataSource.setPassword(password);

    Optional.ofNullable(dataSourceProperties.getMinimum()).ifPresent(hikariDataSource::setMinimumIdle);
    Optional.ofNullable(dataSourceProperties.getMaximum()).ifPresent(hikariDataSource::setMaximumPoolSize);
    Optional.ofNullable(dataSourceProperties.getTimeout()).ifPresent(hikariDataSource::setConnectionTimeout);
    Optional.ofNullable(dataSourceProperties.getIdleTimeout()).ifPresent(hikariDataSource::setIdleTimeout);

    hikariDataSource.addDataSourceProperty("createDatabaseIfNotExist", true);

    return hikariDataSource;
  }

  private static HibernateJpaVendorAdapter getVendorAdapter() {
    return new HibernateJpaVendorAdapter();
  }
}
