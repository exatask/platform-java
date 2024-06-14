package com.exatask.platform.mongodb.filters;

import com.exatask.platform.mongodb.exceptions.InvalidIdentifierException;
import com.exatask.platform.utilities.ResourceUtility;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class FilterElement {

  private final String key;

  private final FilterOperation operation;

  private final Object value;

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
          criteria.in((List<?>) value);
        } else if (value.getClass().isArray()) {
          criteria.in(Arrays.asList((Object[]) value));
        } else if (value.getClass().isEnum()) {
          criteria.is(value.toString());
        } else {
          criteria.is(value);
        }
        break;

      case NOT_EQUAL:
        if (value instanceof List) {
          criteria.nin((List<?>) value);
        } else if (value.getClass().isArray()) {
          criteria.nin(Arrays.asList((Object[]) value));
        } else if (value.getClass().isEnum()) {
          criteria.ne(value.toString());
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
