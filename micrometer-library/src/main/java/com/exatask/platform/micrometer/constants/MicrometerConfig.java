package com.exatask.platform.micrometer.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MicrometerConfig {

  public static final String ELASTIC_HOST = "micrometer.elastic.host";
  public static final String ELASTIC_USERNAME = "micrometer.elastic.username";
  public static final String ELASTIC_PASSWORD = "micrometer.elastic.password";
  public static final String ELASTIC_API_KEY = "micrometer.elastic.api-key";

  public static final String NEWRELIC_ACCOUNT_ID = "micrometer.newrelic.account-id";
  public static final String NEWRELIC_API_KEY = "micrometer.newrelic.api-key";
}
