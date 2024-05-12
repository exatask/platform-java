package com.exatask.platform.subscriber.services.subscribers;

import com.exatask.platform.subscriber.libraries.SubscriberLibrary;
import com.exatask.platform.subscriber.services.AppService;
import com.exatask.platform.subscriber.subscribers.AppSubscriber;
import com.exatask.platform.utilities.ApplicationContextUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import picocli.CommandLine;

@Service("allSubscriberService")
public class AllSubscriberService extends AppService {

  @Autowired
  private SubscriberLibrary subscriberLibrary;

  @CommandLine.Option(
      names = "-all-subscribers",
      description = "Enable and execute all subscribers in the service"
  )
  private boolean allSubscribers;

  @Override
  public Integer call() {

    String[] subscriberBeanNames = ApplicationContextUtility.getBeanNames(AppSubscriber.class);
    for (String subscriber : subscriberBeanNames) {
      Object subscriberBean = ApplicationContextUtility.getBean(subscriber);
      subscriberLibrary.executeSubscriberClass(subscriberBean);
    }

    return 0;
  }
}
