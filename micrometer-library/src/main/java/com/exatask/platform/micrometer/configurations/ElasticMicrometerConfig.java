package com.exatask.platform.micrometer.configurations;

import com.exatask.platform.micrometer.constants.MicrometerConfig;
import com.exatask.platform.utilities.ServiceUtility;
import io.micrometer.elastic.ElasticConfig;

public class ElasticMicrometerConfig implements ElasticConfig {

  @Override
  public String host() {
    return ServiceUtility.getServiceProperty(MicrometerConfig.ELASTIC_HOST, "");
  }

  @Override
  public String userName() {
    return ServiceUtility.getServiceProperty(MicrometerConfig.ELASTIC_USERNAME, "");
  }

  @Override
  public String password() {
    return ServiceUtility.getServiceProperty(MicrometerConfig.ELASTIC_PASSWORD, "");
  }

  @Override
  public String apiKeyCredentials() {
    return ServiceUtility.getServiceProperty(MicrometerConfig.ELASTIC_API_KEY, "");
  }

  @Override
  public String get(String key) {
    return null;
  }
}
