package com.exatask.platform.mysql.filters;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class AppFilter {

  @Getter
  private final List<FilterElement> filters;

  public AppFilter() {
    filters = new ArrayList<>();
  }

  public void addFilter(String key, Object value) {
    addFilter(new FilterElement(key, FilterOperation.EQUAL, value));
  }

  public void addFilter(FilterElement filterElement) {
    filters.add(filterElement);
  }
}
