package com.exatask.platform.postgresql.filters;

import com.exatask.platform.postgresql.AppModel;
import com.exatask.platform.postgresql.exceptions.InvalidFilterException;
import com.exatask.platform.postgresql.utilities.QueryUtility;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.math.NumberUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class FilterElement {

  private final Class<? extends AppModel> model;

  private final String key;

  private final FilterOperation operation;

  private final Object value;

  public Predicate getPredicate(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery) {

    Root from = null;
    Set<Root> roots = criteriaQuery.getRoots();
    for (Root root : roots) {
      if (root.getModel().getJavaType() == model) {
        from = root;
        break;
      }
    }

    if (from == null) {
      from = criteriaQuery.from(model);
    }

    Path path = from.get(key);
    return getPredicate(criteriaBuilder, path);
  }

  public Predicate getPredicate(CriteriaBuilder criteriaBuilder, CriteriaUpdate criteriaUpdate) {

    Root from = criteriaUpdate.getRoot();
    if (from.getModel().getJavaType() != model) {
      from = criteriaUpdate.from(model);
    }

    Path path = from.get(key);
    return getPredicate(criteriaBuilder, path);
  }

  public String getPredicate() {

    String path = QueryUtility.getClassAlias(model) + "." + key;

    switch (operation) {

      case EQUAL:
        if (value == null) {
          return String.format(" %s IS NULL ", path);
        } else if (value instanceof List || value.getClass().isArray()) {
          return String.format(" %s IN ('%s') ", path, String.join("','", (List) value));
        } else {
          return String.format(" %s = '%s' ", path, value);
        }

      case NOT_EQUAL:
        if (value == null) {
          return String.format(" %s IS NOT NULL ", path);
        } else if (value instanceof List || value.getClass().isArray()) {
          return String.format(" %s NOT IN ('%s') ", path, String.join("','", (List) value));
        } else {
          return String.format(" %s != '%s' ", path, value);
        }

      case GREATER:
        if (NumberUtils.isCreatable(value.toString())) {
          return String.format(" %s > %s ", path, NumberUtils.createNumber(value.toString()));
        } else {
          return String.format(" %s > '%s' ", path, value);
        }

      case GREATER_EQUAL:
        if (NumberUtils.isCreatable(value.toString())) {
          return String.format(" %s >= %s ", path, NumberUtils.createNumber(value.toString()));
        } else {
          return String.format(" %s >= '%s' ", path, value);
        }

      case LESSER:
        if (NumberUtils.isCreatable(value.toString())) {
          return String.format(" %s < %s ", path, NumberUtils.createNumber(value.toString()));
        } else {
          return String.format(" %s < '%s' ", path, value);
        }

      case LESSER_EQUAL:
        if (NumberUtils.isCreatable(value.toString())) {
          return String.format(" %s <= %s ", path, NumberUtils.createNumber(value.toString()));
        } else {
          return String.format(" %s <= '%s' ", path, value);
        }

      case REGEX:
        return String.format(" %s LIKE '%s' ", path, value.toString());

      case NOT_REGEX:
        return String.format(" %s NOT LIKE '%s' ", path, value.toString());

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
