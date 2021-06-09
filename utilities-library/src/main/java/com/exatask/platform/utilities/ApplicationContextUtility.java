package com.exatask.platform.utilities;

import lombok.Getter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class ApplicationContextUtility implements ApplicationContextAware {

  @Getter
  private static ApplicationContext applicationContext;

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    ApplicationContextUtility.applicationContext = applicationContext;
  }

  public static Object getBean(String beanName) {
    return applicationContext.getBean(beanName);
  }

  public static <T> T getBean(Class<T> beanClass) {
    return applicationContext.getBean(beanClass);
  }
}
