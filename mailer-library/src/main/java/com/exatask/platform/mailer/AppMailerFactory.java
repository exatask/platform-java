package com.exatask.platform.mailer;

import com.exatask.platform.mailer.exceptions.InvalidMailerException;
import com.exatask.platform.mailer.templates.TemplateEngine;
import com.exatask.platform.mailer.transports.aws.AwsMailer;
import com.exatask.platform.mailer.transports.smtp.SmtpMailer;
import com.exatask.platform.utilities.properties.AwsProperties;
import com.exatask.platform.utilities.properties.SmtpProperties;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class AppMailerFactory {

  private static final Map<Mailer, AppMailer> mailerList = new HashMap<>();

  public static AppMailer getInstance(Mailer mailer, Object properties, TemplateEngine templateEngine) {

    if (mailer == null) {
      throw new InvalidMailerException("null");
    }

    if (mailerList.containsKey(mailer)) {
      return mailerList.get(mailer);
    }

    AppMailer appMailer;
    switch (mailer) {

      case SMTP:
        appMailer = new SmtpMailer((SmtpProperties) properties, templateEngine);
        break;

      case AWS:
        appMailer = new AwsMailer((AwsProperties) properties, templateEngine);
        break;

      default:
        throw new InvalidMailerException(mailer.toString());
    }

    mailerList.put(mailer, appMailer);
    return appMailer;
  }
}
