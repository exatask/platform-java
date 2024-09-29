package com.exatask.platform.migrate.system.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ResourceUtils;

@Configuration
@PropertySource(ResourceUtils.CLASSPATH_URL_PREFIX + "application-migrate.properties")
public class MigrateAutoConfiguration {
}
