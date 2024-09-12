package com.exatask.platform.elasticsearch.queries.filters;

import com.exatask.platform.elasticsearch.system.exceptions.InvalidIdentifierException;
import com.exatask.platform.utilities.ResourceUtility;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.elasticsearch.core.query.Criteria;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public class FilterElement {

  private final String key;

  private final FilterOperation operation;

  private final Object value;

  public FilterElement(String key, Object value) {
    this(key, FilterOperation.EQUAL, value);
  }

  public static FilterElement identifier(String value) {

    if (StringUtils.isNumeric(value)) {
      return new FilterElement("id", FilterOperation.EQUAL, Integer.parseInt(value));
    } else if (ResourceUtility.isUrn(value)) {
      return new FilterElement("urn", FilterOperation.EQUAL, value);
    } else {
      throw new InvalidIdentifierException(value);
    }
  }

  public Criteria getCriteria() {

    Criteria criteria = new Criteria(key);

    switch (operation) {

      case EQUAL:
        if (value instanceof List) {
          return criteria.in((List<?>) value);
        } else if (value.getClass().isArray()) {
          return criteria.in(Arrays.asList((Object[]) value));
        } else if (value.getClass().isEnum()) {
          return criteria.is(value.toString());
        } else {
          return criteria.is(value);
        }

      case NOT_EQUAL:
        if (value instanceof List) {
          return criteria.notIn((List<?>) value);
        } else if (value.getClass().isArray()) {
          return criteria.notIn(Arrays.asList((Object[]) value));
        } else if (value.getClass().isEnum()) {
          return criteria.is(value.toString()).not();
        } else {
          return criteria.is(value).not();
        }

      case MATCHES:
        if (value instanceof List) {
          return criteria.matchesAll(value);
        } else if (value.getClass().isArray()) {
          return criteria.matchesAll(Arrays.asList((Object[]) value));
        } else if (value.getClass().isEnum()) {
          return criteria.matches(value.toString());
        } else {
          return criteria.matches(value);
        }

      case NOT_MATCHES:
        if (value instanceof List) {
          return criteria.matchesAll(value).not();
        } else if (value.getClass().isArray()) {
          return criteria.matchesAll(Arrays.asList((Object[]) value)).not();
        } else if (value.getClass().isEnum()) {
          return criteria.matches(value.toString()).not();
        } else {
          return criteria.matches(value).not();
        }

      case GREATER:
        return criteria.greaterThan(value);

      case GREATER_EQUAL:
        return criteria.greaterThanEqual(value);

      case LESSER:
        return criteria.lessThan(value);

      case LESSER_EQUAL:
        return criteria.lessThanEqual(value);

      case EXISTS:
        criteria = criteria.exists();
        if (!Boolean.parseBoolean(value.toString())) {
          criteria = criteria.not();
        }
        return criteria;
    }

    return criteria;
  }
}

