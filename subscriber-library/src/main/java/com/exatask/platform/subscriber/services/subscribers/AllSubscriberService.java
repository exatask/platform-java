package com.exatask.platform.subscriber.services.subscribers;

import com.exatask.platform.subscriber.libraries.SubscriberLibrary;
import com.exatask.platform.subscriber.services.AppService;
import com.exatask.platform.subscriber.subscribers.AppSubscriber;
import com.exatask.platform.utilities.ApplicationContextUtility;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("allSubscriberService")
public class AllSubscriberService extends AppService {

  @Autowired
  private SubscriberLibrary subscriberLibrary;

  @Override
  public String option() {
    return "all-subscribers";
  }

  @Override
  public List<Option> options() {

    List<Option> options = new ArrayList<>();

    options.add(Option.builder()
        .longOpt("all-subscribers")
        .desc("Enable and execute all subscribers in the service")
        .hasArg(false)
        .build());

    return options;
  }

  @Override
  public void process(CommandLine commandLine) {

    String[] subscriberBeanNames = ApplicationContextUtility.getBeanNames(AppSubscriber.class);
    for (String subscriber : subscriberBeanNames) {
      Object subscriberBean = ApplicationContextUtility.getBean(subscriber);
      subscriberLibrary.executeSubscriberClass(subscriberBean);
    }
  }
}
