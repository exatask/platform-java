package com.exatask.platform.jpa.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ResourceUtils;

@Configuration
@PropertySource(ResourceUtils.CLASSPATH_URL_PREFIX + "application-jpa.properties")
public class JpaAutoConfiguration {
}
