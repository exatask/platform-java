package com.exatask.platform.subscriber;

import com.exatask.platform.subscriber.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class SubscriberRunner implements CommandLineRunner {

  @Autowired
  private Set<AppService> subscriberServices;

  @Override
  public void run(String... args) {

    for (AppService subscriberService : subscriberServices) {
      subscriberService.getCommandLine().execute(args);
    }
  }
}
