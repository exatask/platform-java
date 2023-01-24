package com.exatask.platform.utilities;

import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@UtilityClass
public class CollectionUtility {

  public static <T> Set<T> nullSafe(Set<T> set) {
    return Optional.ofNullable(set).orElse(Collections.emptySet());
  }

  public static <T> List<T> nullSafe(List<T> list) {
    return Optional.ofNullable(list).orElse(Collections.emptyList());
  }

  public static <K, V> Map<K, V> nullSafe(Map<K, V> map) {
    return Optional.ofNullable(map).orElse(Collections.emptyMap());
  }

  public static <T> Set<T> defaultIfNull(Set<T> set, Set<T> defaultSet) {
    return Optional.ofNullable(set).orElse(defaultSet);
  }

  public static <T> List<T> defaultIfNull(List<T> list, List<T> defaultList) {
    return Optional.ofNullable(list).orElse(defaultList);
  }

  public static <K, V> Map<K, V> defaultIfNull(Map<K, V> map, Map<K, V> defaultMap) {
    return Optional.ofNullable(map).orElse(defaultMap);
  }

  public static <T> Set<T> defaultIfEmpty(Set<T> set, Set<T> defaultSet) {
    return !CollectionUtils.isEmpty(set) ? set : defaultSet;
  }

  public static <T> List<T> defaultIfEmpty(List<T> list, List<T> defaultList) {
    return !CollectionUtils.isEmpty(list) ? list : defaultList;
  }

  public static <K, V> Map<K, V> defaultIfEmpty(Map<K, V> map, Map<K, V> defaultMap) {
    return !CollectionUtils.isEmpty(map) ? map : defaultMap;
  }
}
