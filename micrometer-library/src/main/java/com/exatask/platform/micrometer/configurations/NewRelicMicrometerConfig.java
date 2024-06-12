package com.exatask.platform.micrometer.configurations;

import com.exatask.platform.micrometer.constants.MicrometerConfig;
import com.exatask.platform.utilities.ServiceUtility;
import io.micrometer.newrelic.NewRelicConfig;

public class NewRelicMicrometerConfig implements NewRelicConfig {

  @Override
  public String accountId() {
    return ServiceUtility.getServiceProperty(MicrometerConfig.NEWRELIC_ACCOUNT_ID, "");
  }

  @Override
  public String apiKey() {
    return ServiceUtility.getServiceProperty(MicrometerConfig.NEWRELIC_API_KEY, "");
  }

  @Override
  public String get(String key) {
    return null;
  }
}
