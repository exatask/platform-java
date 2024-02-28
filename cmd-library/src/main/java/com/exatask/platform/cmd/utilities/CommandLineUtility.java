package com.exatask.platform.cmd.utilities;

import com.exatask.platform.cmd.constants.SubscriberOptions;
import com.exatask.platform.cmd.entities.Subscriber;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import lombok.experimental.UtilityClass;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Collections;

@UtilityClass
public class CommandLineUtility {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  private static final String COMMAND_LINE_SUBSCRIBER_ACTION_SEPARATOR = "@";

  public static Subscriber parseSubscriberAction(String subscriberAction) {

    String[] subscriberActionParts = subscriberAction.split(COMMAND_LINE_SUBSCRIBER_ACTION_SEPARATOR, 2);
    if (subscriberActionParts.length != 2) {
      LOGGER.warn("Empty subscriber or action provided", Collections.singletonMap("subscriber-action", subscriberAction));
      return null;
    }

    return new Subscriber(subscriberActionParts[0], subscriberActionParts[1]);
  }

  public static Options getSubscriberOptions() {

    Options options = new Options();
    options.addOption(new Option(SubscriberOptions.DISABLE_API, false, "Disables API execution in the service"))
        .addOption(new Option(SubscriberOptions.SUBSCRIBER_SHORT, SubscriberOptions.SUBSCRIBER, true, "Subscriber Class to be loaded and all the actions within the class to be executed"))
        .addOption(new Option(SubscriberOptions.SUBSCRIBER_ACTION_SHORT, SubscriberOptions.SUBSCRIBER_ACTION, true, "Subscriber Action within the specified class to be loaded and executed"))
        .addOption(new Option(SubscriberOptions.ALL_SUBSCRIBERS, false, "Enable and execute all subscribers in the service"));
    return options;
  }
}
