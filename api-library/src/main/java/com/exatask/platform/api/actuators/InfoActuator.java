package com.exatask.platform.api.actuators;

import com.exatask.platform.api.constants.ApiService;
import com.exatask.platform.utilities.ServiceUtility;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class InfoActuator implements InfoContributor {

  @Override
  public void contribute(Info.Builder builder) {

    builder.withDetail("name", ServiceUtility.getServiceName());
    builder.withDetail("copyright", ApiService.COPYRIGHT);
    builder.withDetail("environment", ServiceUtility.getServiceEnvironment());
    builder.withDetail("license", ApiService.LICENSE);
    builder.withDetail("version", ServiceUtility.getServiceVersion());
  }
}
