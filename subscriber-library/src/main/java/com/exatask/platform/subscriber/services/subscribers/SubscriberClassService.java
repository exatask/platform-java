package com.exatask.platform.subscriber.services.subscribers;

import com.exatask.platform.subscriber.libraries.SubscriberLibrary;
import com.exatask.platform.subscriber.services.AppService;
import com.exatask.platform.utilities.ApplicationContextUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import picocli.CommandLine;

@Service("subscriberClassService")
public class SubscriberClassService extends AppService {

  @Autowired
  private SubscriberLibrary subscriberLibrary;

  @CommandLine.Option(
      names = "-subscriber-class",
      description = "Subscriber Class to be loaded and all the actions within the class to be executed"
  )
  private String[] subscriberClasses;

  @Override
  public Integer call() {

    for (String subscriber : subscriberClasses) {
      Object subscriberBean = ApplicationContextUtility.getBean(subscriber);
      subscriberLibrary.executeSubscriberClass(subscriberBean);
    }

    return 0;
  }
}
