package com.exatask.platform.rabbitmq.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ResourceUtils;

@Configuration
@PropertySource(ResourceUtils.CLASSPATH_URL_PREFIX + "application-rabbitmq.properties")
public class RabbitmqAutoConfiguration {
}
