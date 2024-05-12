package com.exatask.platform.subscriber.services.subscribers;

import com.exatask.platform.subscriber.entities.Subscriber;
import com.exatask.platform.subscriber.libraries.SubscriberLibrary;
import com.exatask.platform.subscriber.services.AppService;
import com.exatask.platform.utilities.ApplicationContextUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import picocli.CommandLine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

@Service("subscriberMethodService")
public class SubscriberMethodService extends AppService {

  private static final String COMMAND_LINE_SUBSCRIBER_ACTION_SEPARATOR = "@";

  @Autowired
  private SubscriberLibrary subscriberLibrary;

  @CommandLine.Option(
      names = "-subscriber-method",
      description = "Subscriber Action within the specified class to be loaded and executed"
  )
  private String[] subscriberMethods;

  @Override
  public Integer call() {

    for (String subscriberMethod : subscriberMethods) {

      Subscriber subscriberInfo = parseSubscriberAction(subscriberMethod);
      if (ObjectUtils.isEmpty(subscriberInfo)) {
        LOGGER.warn("Provided subscriber-method information is invalid", Collections.singletonMap("subscriber-method", subscriberMethod));
        continue;
      }

      Object subscriberBean = ApplicationContextUtility.getBean(subscriberInfo.getSubscriber());
      Method beanMethod;

      try {
        beanMethod = subscriberBean.getClass().getMethod(subscriberInfo.getAction());
        subscriberLibrary.executeSubscriberMethod(subscriberBean, beanMethod);

      } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
        LOGGER.error(exception);
      }
    }

    return 0;
  }

  private Subscriber parseSubscriberAction(String subscriberAction) {

    String[] subscriberActionParts = subscriberAction.split(COMMAND_LINE_SUBSCRIBER_ACTION_SEPARATOR, 2);
    if (subscriberActionParts.length != 2) {
      LOGGER.warn("Empty subscriber or action provided", Collections.singletonMap("subscriber-action", subscriberAction));
      return null;
    }

    return new Subscriber(subscriberActionParts[0], subscriberActionParts[1]);
  }
}
