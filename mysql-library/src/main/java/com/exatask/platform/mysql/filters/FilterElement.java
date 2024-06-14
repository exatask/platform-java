package com.exatask.platform.mysql.filters;

import com.exatask.platform.mysql.AppModel;
import com.exatask.platform.mysql.exceptions.InvalidFilterException;
import com.exatask.platform.mysql.exceptions.InvalidIdentifierException;
import com.exatask.platform.mysql.utilities.QueryUtility;
import com.exatask.platform.utilities.ResourceUtility;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class FilterElement {

  private Class<? extends AppModel> model;

  private String key;

  private FilterOperation operation;

  private Object value;

  private FilterType type;

  private List<FilterElement> elements;

  public FilterElement(Class<? extends AppModel> model, String key, FilterOperation operation, Object value) {

    this.model = model;
    this.key = key;
    this.operation = operation;
    this.value = value;
  }

  public FilterElement(FilterType type, List<FilterElement> elements) {

    this.type = type;
    this.elements = elements;
  }

  public static FilterElement identifier(Class<? extends AppModel> model, String value) {

    if (StringUtils.isNumeric(value)) {
      return new FilterElement(model, "id", FilterOperation.EQUAL, Integer.parseInt(value));
    } else if (ResourceUtility.isUrn(value)) {
      return new FilterElement(model, "urn", FilterOperation.EQUAL, value);
    } else {
      throw new InvalidIdentifierException(value);
    }
  }

  public Predicate getPredicate(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery) {

    if (!CollectionUtils.isEmpty(this.elements)) {

      List<Predicate> predicates = new ArrayList<>();
      for (FilterElement element : this.elements) {
        predicates.add(element.getPredicate(criteriaBuilder, criteriaQuery));
      }

      if (type == FilterType.AND) {
        return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
      } else {
        return criteriaBuilder.or(predicates.toArray(new Predicate[]{}));
      }
    }

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

    if (!CollectionUtils.isEmpty(this.elements)) {

      List<Predicate> predicates = new ArrayList<>();
      for (FilterElement element : this.elements) {
        predicates.add(element.getPredicate(criteriaBuilder, criteriaUpdate));
      }

      if (type == FilterType.AND) {
        return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
      } else {
        return criteriaBuilder.or(predicates.toArray(new Predicate[]{}));
      }
    }

    Root from = criteriaUpdate.getRoot();
    if (from.getModel().getJavaType() != model) {
      from = criteriaUpdate.from(model);
    }

    Path path = from.get(key);
    return getPredicate(criteriaBuilder, path);
  }

  public String getPredicate() {

    if (!CollectionUtils.isEmpty(elements)) {

      StringBuilder query = new StringBuilder("(");
      int length = elements.size();

      for (int i = 0; i < elements.size(); i++) {

        query.append(elements.get(i).getPredicate());
        if (i != (length - 1)) {
          query.append(type == FilterType.AND ? " AND " : " OR ");
        }
      }

      query.append(")");
      return query.toString();
    }

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
