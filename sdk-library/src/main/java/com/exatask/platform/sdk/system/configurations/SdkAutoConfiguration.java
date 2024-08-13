package com.exatask.platform.sdk.system.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.ResourceUtils;

@Configuration
@PropertySource(ResourceUtils.CLASSPATH_URL_PREFIX + "application-sdk.properties")
public class SdkAutoConfiguration {
}
