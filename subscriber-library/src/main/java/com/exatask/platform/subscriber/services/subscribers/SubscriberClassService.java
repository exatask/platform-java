package com.exatask.platform.subscriber.services.subscribers;

import com.exatask.platform.subscriber.libraries.SubscriberLibrary;
import com.exatask.platform.subscriber.services.AppService;
import com.exatask.platform.utilities.ApplicationContextUtility;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("subscriberClassService")
public class SubscriberClassService extends AppService {

  @Autowired
  private SubscriberLibrary subscriberLibrary;

  @Override
  public String option() {
    return "subscriber-class";
  }

  @Override
  public List<Option> options() {

    List<Option> options = new ArrayList<>();

    options.add(Option.builder()
        .longOpt("subscriber-class")
        .desc("Subscriber Class to be loaded and all the actions within the class to be executed")
        .hasArg(true)
        .valueSeparator(',')
        .build());

    return options;
  }

  @Override
  public void process(CommandLine commandLine) {

    String[] allSubscribers = commandLine.getOptionValues("subscriber-class");
    for (String subscriber : allSubscribers) {

      Object subscriberBean = ApplicationContextUtility.getBean(subscriber);
      subscriberLibrary.executeSubscriberClass(subscriberBean);
    }
  }
}
