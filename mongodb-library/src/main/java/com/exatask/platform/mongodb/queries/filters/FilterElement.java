package com.exatask.platform.mongodb.queries.filters;

import com.exatask.platform.mongodb.system.exceptions.InvalidIdentifierException;
import com.exatask.platform.utilities.IdentifierUtility;
import com.exatask.platform.utilities.ResourceUtility;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class FilterElement {

  private final String key;

  private final FilterOperation operation;

  private final Object value;

  public FilterElement(String key, Object value) {
    this(key, FilterOperation.EQUAL, value);
  }

  public static FilterElement identifier(String value) {

    if (ObjectId.isValid(value)) {
      return new FilterElement("_id", new ObjectId(value));
    } else if (StringUtils.isNumeric(value)) {
      return new FilterElement("_id", Integer.parseInt(value));
    } else if (IdentifierUtility.isUuid(value)) {
      return new FilterElement("_id", value);
    } else if (ResourceUtility.isUrn(value)) {
      return new FilterElement("urn", value);
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
          return criteria.nin((List<?>) value);
        } else if (value.getClass().isArray()) {
          return criteria.nin(Arrays.asList((Object[]) value));
        } else if (value.getClass().isEnum()) {
          return criteria.ne(value.toString());
        } else {
          return criteria.ne(value);
        }

      case GREATER:
        return criteria.gt(value);

      case GREATER_EQUAL:
        return criteria.gte(value);

      case LESSER:
        return criteria.lt(value);

      case LESSER_EQUAL:
        return criteria.lte(value);

      case REGEX:
        return criteria.regex(value.toString());

      case NOT_REGEX:
        return criteria.not().regex(value.toString());

      case EXISTS:
        return criteria.exists(Boolean.parseBoolean(value.toString()));
    }

    return criteria;
  }
}
