package com.exatask.platform.mysql.filters;

import com.exatask.platform.mysql.AppModel;
import com.exatask.platform.mysql.exceptions.InvalidFilterException;
import com.exatask.platform.mysql.utilities.QueryUtility;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.List;

@AllArgsConstructor
public class FilterElement {

  private final Class<? extends AppModel> model;

  private final String key;

  private final FilterOperation operation;

  private final Object value;

  public Predicate getPredicate(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery) {

    Path path = criteriaQuery.from(model).get(key);
    return getPredicate(criteriaBuilder, path);
  }

  public Predicate getPredicate(CriteriaBuilder criteriaBuilder, CriteriaUpdate criteriaUpdate) {

    Path path = criteriaUpdate.from(model).get(key);
    return getPredicate(criteriaBuilder, path);
  }

  public String getPredicate() {

    String path = QueryUtility.getClassAlias(model) + "." + key;

    switch (operation) {

      case EQUAL:
        if (value == null) {
          return path + " IS NULL ";
        } else if (value instanceof List || value.getClass().isArray()) {
          return path + " IN (" + String.join(",", (List) value) + ")";
        } else {
          return path + " = " + value;
        }

      case NOT_EQUAL:
        if (value == null) {
          return path + " IS NOT NULL ";
        } else if (value instanceof List || value.getClass().isArray()) {
          return path + " NOT IN (" + String.join(",", (List) value) + ")";
        } else {
          return path + " != " + value;
        }

      case GREATER:
        if (NumberUtils.isCreatable(value.toString())) {
          return path + " > " + NumberUtils.createNumber(value.toString());
        } else {
          return path + " > " + value;
        }

      case GREATER_EQUAL:
        if (NumberUtils.isCreatable(value.toString())) {
          return path + " >= " + NumberUtils.createNumber(value.toString());
        } else {
          return path + " >= " + value;
        }

      case LESSER:
        if (NumberUtils.isCreatable(value.toString())) {
          return path + " < " + NumberUtils.createNumber(value.toString());
        } else {
          return path + " < " + value;
        }

      case LESSER_EQUAL:
        if (NumberUtils.isCreatable(value.toString())) {
          return path + " <= " + NumberUtils.createNumber(value.toString());
        } else {
          return path + " <= " + value;
        }

      case REGEX:
        return path + " LIKE " + value.toString();

      case NOT_REGEX:
        return path + " NOT LIKE " + value.toString();

      default:
        throw new InvalidFilterException(operation.toString());
    }
  }

  private Predicate getPredicate(CriteriaBuilder criteriaBuilder, Path path) {

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
