package com.exatask.platform.mailer.transports;

import com.exatask.platform.mailer.exceptions.InvalidMailerException;
import com.exatask.platform.mailer.templates.AppTemplateEngine;
import com.exatask.platform.utilities.properties.AwsProperties;
import com.exatask.platform.utilities.properties.SshProperties;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class AppTransportFactory {

  private static final Map<AppTransportType, AppTransport> mailerList = new HashMap<>();

  public static AppTransport getInstance(AppTransportType transportType, Object properties, AppTemplateEngine templateEngine) {

    if (transportType == null) {
      throw new InvalidMailerException("null");
    }

    if (mailerList.containsKey(transportType)) {
      return mailerList.get(transportType);
    }

    AppTransport appTransport;
    switch (transportType) {

      case SMTP:
        appTransport = new SmtpTransport((SshProperties) properties, templateEngine);
        break;

      case AWS:
        appTransport = new AwsTransport((AwsProperties) properties, templateEngine);
        break;

      default:
        throw new InvalidMailerException(transportType.toString());
    }

    mailerList.put(transportType, appTransport);
    return appTransport;
  }
}
