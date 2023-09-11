package com.exatask.platform.subscriber;

import com.exatask.platform.subscriber.constants.CommandLine;
import com.exatask.platform.subscriber.subscribers.AppSubscriber;
import com.exatask.platform.subscriber.entities.Subscriber;
import com.exatask.platform.subscriber.utilities.CommandLineUtility;
import com.exatask.platform.logging.AppLogManager;
import com.exatask.platform.logging.AppLogger;
import com.exatask.platform.utilities.ApplicationContextUtility;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SubscriberRunner implements CommandLineRunner {

  private static final AppLogger LOGGER = AppLogManager.getLogger();

  @Override
  public void run(String... args) throws Exception {

    Map<String, List<String>> commandLineArgs = CommandLineUtility.parseArguments(args);

    if (commandLineArgs.containsKey(CommandLine.ALL_SUBSCRIBERS)) {
      executeAllSubscribers(commandLineArgs.get(CommandLine.ALL_SUBSCRIBERS).get(0).trim());

    } else if (commandLineArgs.containsKey(CommandLine.SUBSCRIBER)) {

      List<String> allSubscribers = commandLineArgs.get(CommandLine.SUBSCRIBER);
      for (String subscriber : allSubscribers) {
        executeSubscriber(subscriber);
      }

    } else if (commandLineArgs.containsKey(CommandLine.SUBSCRIBER_ACTION)) {

      List<String> allSubscriberActions = commandLineArgs.get(CommandLine.SUBSCRIBER_ACTION);
      for (String subscriberAction : allSubscriberActions) {
        executeSubscriberAction(subscriberAction);
      }
    }
  }

  private void executeAllSubscribers(String value) {

    if (!value.equalsIgnoreCase("true") && !value.equals("1")) {
      LOGGER.warn("all-subscribers argument mentioned with invalid value", Collections.singletonMap("value", value));
      return;
    }

    String[] subscriberBeanNames = ApplicationContextUtility.getBeanNames(AppSubscriber.class);
    for (String subscriber : subscriberBeanNames) {

      Object subscriberBean = ApplicationContextUtility.getBean(subscriber);
      Method[] beanMethods = subscriberBean.getClass().getMethods();
      for (Method beanMethod : beanMethods) {

        try {
          executeSubscriberAction(subscriberBean, beanMethod);
        } catch (InvocationTargetException | IllegalAccessException exception) {
          LOGGER.error(exception);
        }
      }
    }
  }

  private void executeSubscriber(String subscriber) {

    Object subscriberBean = ApplicationContextUtility.getBean(subscriber);
    Method[] beanMethods = subscriberBean.getClass().getDeclaredMethods();

    for (Method beanMethod : beanMethods) {
      try {
        executeSubscriberAction(subscriberBean, beanMethod);
      } catch (InvocationTargetException | IllegalAccessException exception) {
        LOGGER.error(exception);
      }
    }
  }

  private void executeSubscriberAction(String subscriberAction) {

    Subscriber subscriberInfo = CommandLineUtility.parseSubscriberAction(subscriberAction);
    if (ObjectUtils.isEmpty(subscriberInfo)) {
      LOGGER.warn("Provided subscriber-action information is invalid", Collections.singletonMap("subscriber-action", subscriberAction));
      return;
    }

    Object subscriberBean = ApplicationContextUtility.getBean(subscriberInfo.getSubscriber());
    Method beanMethod;

    try {
      beanMethod = subscriberBean.getClass().getMethod(subscriberInfo.getAction());
      executeSubscriberAction(subscriberBean, beanMethod);

    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException exception) {
      LOGGER.error(exception);
    }
  }

  private void executeSubscriberAction(Object bean, Method beanMethod) throws InvocationTargetException, IllegalAccessException {

    Map<String, Object> extraParams = new HashMap<>();
    extraParams.put("subscriber", bean.getClass());
    extraParams.put("action", beanMethod.getName());

    if (ObjectUtils.isEmpty(bean) || ObjectUtils.isEmpty(beanMethod)) {
      LOGGER.error("Invalid/Empty Bean and method provided for invocation");
      return;

    } else if (!(bean instanceof AppSubscriber)) {
      LOGGER.error("Subscriber bean is not an instance of AppSubscriber base class", Collections.singletonMap("subscriber", bean.getClass()));
      return;

    } else if (bean.getClass() != beanMethod.getDeclaringClass()) {
      LOGGER.warn("Action doesn't belong to subscriber bean", extraParams);
      return;

    } else if (!Modifier.isPublic(beanMethod.getModifiers())) {
      LOGGER.error("Action is not accessible on provided subscriber bean", extraParams);
      return;
    }

    beanMethod.invoke(bean);
    LOGGER.info("Action invoked successfully on subscriber bean", extraParams);
  }
}
