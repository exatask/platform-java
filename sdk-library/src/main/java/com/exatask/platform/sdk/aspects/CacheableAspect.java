package com.exatask.platform.sdk.aspects;

import com.exatask.platform.sdk.aspects.annotations.Cacheable;
import com.exatask.platform.sdk.caches.AppServiceCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class CacheableAspect {

  @Autowired
  ApplicationContext applicationContext;

  @Pointcut("execution(public * com.exatask.platform.sdk.clients.AppServiceClient+.*(..))")
  public void cacheable() {}

  @Around("cacheable()")
  public Object cache(ProceedingJoinPoint joinPoint) throws Throwable {

    // Fetch Method signature and method reflection instance
    MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
    Method method = methodSignature.getMethod();

    // Find Cacheable annotation instance associated with method
    // If not found, proceed ahead with actual method execution
    Cacheable cacheable = AnnotatedElementUtils.findMergedAnnotation(method, Cacheable.class);
    if (cacheable == null) {
      return joinPoint.proceed();
    }

    // Obtain bean associated with AppServiceCache implementation mentioned in Cacheable annotation
    AppServiceCache serviceCache = applicationContext.getBean(cacheable.value());

    // Invoke get method of AppServiceCache instance, if result is obtained return after casting
    Object cachedResult = serviceCache.get(joinPoint.getArgs());
    if (cachedResult != null) {
      return methodSignature.getReturnType().cast(cachedResult);
    }

    // If no result is obtained, proceed with actual method execution
    // If cache property is true in Cacheable annotation, invoke set method of AppServiceCache instance
    Object result = joinPoint.proceed();
    if (cacheable.cache()) {
      serviceCache.set(result, joinPoint.getArgs());
    }

    // Returned obtained result
    return result;
  }
}
