package com.exatask.platform.subscriber.libraries;

import com.exatask.platform.subscriber.subscribers.AppSubscriber;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class SubscriberLibrary extends AppLibrary {

  public void executeSubscriberClass(Object bean) {

    Method[] beanMethods = bean.getClass().getDeclaredMethods();
    for (Method beanMethod : beanMethods) {
      try {
        executeSubscriberMethod(bean, beanMethod);
      } catch (InvocationTargetException | IllegalAccessException exception) {
        LOGGER.error(exception);
      }
    }
  }

  public void executeSubscriberMethod(Object bean, Method beanMethod) throws InvocationTargetException, IllegalAccessException {

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
