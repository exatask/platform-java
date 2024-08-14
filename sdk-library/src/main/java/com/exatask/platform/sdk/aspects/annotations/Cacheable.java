package com.exatask.platform.sdk.aspects.annotations;

import com.exatask.platform.sdk.caches.AppServiceCache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface Cacheable {

  // Implementation Class which will allow caching capabilities
  Class<? extends AppServiceCache> value();

  // Whether to cache the results or not after fetching
  boolean cache() default false;
}
