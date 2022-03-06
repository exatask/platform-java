package com.exatask.platform.mongodb.filters;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class FilterElement {

  private final String key;

  private final FilterOperation operation;

  private final Object value;

  public Criteria getCriteria() {

    Criteria criteria = new Criteria(key);

    switch (operation) {

      case EQUAL:
        if (value instanceof List) {
          criteria.in((List<?>) value);
        } else if (value.getClass().isArray()) {
          criteria.in(Arrays.asList((Object[]) value));
        } else {
          criteria.is(value);
        }
        break;

      case NOT_EQUAL:
        if (value instanceof List) {
          criteria.nin((List<?>) value);
        } else if (value.getClass().isArray()) {
          criteria.nin(Arrays.asList((Object[]) value));
        } else {
          criteria.ne(value);
        }
        break;

      case GREATER:
        criteria.gt(value);
        break;

      case GREATER_EQUAL:
        criteria.gte(value);
        break;

      case LESSER:
        criteria.lt(value);
        break;

      case LESSER_EQUAL:
        criteria.lte(value);
        break;

      case REGEX:
        criteria.regex(value.toString());
        break;

      case NOT_REGEX:
        criteria.not().regex(value.toString());
        break;

      case EXISTS:
        criteria.exists(Boolean.parseBoolean(value.toString()));
        break;
    }

    return criteria;
  }
}
