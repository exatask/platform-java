package com.exatask.platform.mysql.filters;

import com.exatask.platform.mysql.exceptions.InvalidFilterException;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@AllArgsConstructor
public class FilterElement {

  private final String key;

  private final FilterOperation operation;

  private final Object value;

  public Predicate getPredicate(CriteriaBuilder criteriaBuilder, Root from) {

    Path path = from.get(key);

    switch (operation) {

      case EQUAL:
        if (value == null) {
          return criteriaBuilder.isNull(path);
        } else if (value instanceof List || value.getClass().isArray()) {
          return path.in(value);
        } else {
          return criteriaBuilder.equal(path, value);
        }

      case NOT_EQUAL:
        if (value == null) {
          return criteriaBuilder.isNotNull(path);
        } else if (value instanceof List || value.getClass().isArray()) {
          return path.in(value).not();
        } else {
          return criteriaBuilder.equal(path, value).not();
        }

      case GREATER:
        if (NumberUtils.isCreatable(value.toString())) {
          return criteriaBuilder.gt(path, NumberUtils.createNumber(value.toString()));
        } else {
          return criteriaBuilder.greaterThan(path, value.toString());
        }

      case GREATER_EQUAL:
        if (NumberUtils.isCreatable(value.toString())) {
          return criteriaBuilder.ge(path, NumberUtils.createNumber(value.toString()));
        } else {
          return criteriaBuilder.greaterThanOrEqualTo(path, value.toString());
        }

      case LESSER:
        if (NumberUtils.isCreatable(value.toString())) {
          return criteriaBuilder.lt(path, NumberUtils.createNumber(value.toString()));
        } else {
          return criteriaBuilder.lessThan(path, value.toString());
        }

      case LESSER_EQUAL:
        if (NumberUtils.isCreatable(value.toString())) {
          return criteriaBuilder.le(path, NumberUtils.createNumber(value.toString()));
        } else {
          return criteriaBuilder.lessThanOrEqualTo(path, value.toString());
        }

      case REGEX:
        return criteriaBuilder.like(path, value.toString());

      case NOT_REGEX:
        return criteriaBuilder.like(path, value.toString()).not();

      default:
        throw new InvalidFilterException(operation.toString());
    }
  }
}
