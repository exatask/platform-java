package com.exatask.platform.mailer;

import com.exatask.platform.mailer.aws.AppAwsMailer;
import com.exatask.platform.mailer.exceptions.InvalidMailerException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class AppMailerFactory {

  private static final AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(AppMailerFactory.class);

  private static final Map<Mailer, AppMailer> mailerList = new HashMap<>();

  private AppMailerFactory() {
  }

  public AppMailer getInstance(Mailer mailer) {

    if (mailer == null) {
      throw new InvalidMailerException("null");
    }

    if (mailerList.containsKey(mailer)) {
      return mailerList.get(mailer);
    }

    AppMailer appMailer;
    switch (mailer) {

      case AWS:
        appMailer = applicationContext.getBean(AppAwsMailer.class);
        break;

      default:
        throw new InvalidMailerException(mailer.toString());
    }

    mailerList.put(mailer, appMailer);
    return appMailer;
  }
}
