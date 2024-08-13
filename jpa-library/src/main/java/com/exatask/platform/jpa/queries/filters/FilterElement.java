package com.exatask.platform.jpa.queries.filters;

import com.exatask.platform.jpa.AppModel;
import com.exatask.platform.jpa.system.exceptions.InvalidFilterException;
import com.exatask.platform.jpa.system.exceptions.InvalidIdentifierException;
import com.exatask.platform.jpa.utilities.QueryUtility;
import com.exatask.platform.utilities.ResourceUtility;
import lombok.Builder;
import lombok.Singular;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CommonAbstractCriteria;
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

  public FilterElement(Class<? extends AppModel> model, String key, Object value) {
    this(model, key, FilterOperation.EQUAL, value);
  }

  public FilterElement(Class<? extends AppModel> model, String key, FilterOperation operation, Object value) {

    this.model = model;
    this.key = key;
    this.operation = operation;
    this.value = value;
  }

  @Builder
  public FilterElement(FilterType type, @Singular List<FilterElement> elements) {

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
      return getNestedPredicate(criteriaBuilder, criteriaQuery);
    } else {

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
  }

  public Predicate getPredicate(CriteriaBuilder criteriaBuilder, CriteriaUpdate criteriaUpdate) {

    if (!CollectionUtils.isEmpty(this.elements)) {
      return getNestedPredicate(criteriaBuilder, criteriaUpdate);
    } else {

      Root from = criteriaUpdate.getRoot();
      if (from.getModel().getJavaType() != model) {
        from = criteriaUpdate.from(model);
      }

      Path path = from.get(key);
      return getPredicate(criteriaBuilder, path);
    }
  }

  public String getPredicate() {

    if (!CollectionUtils.isEmpty(elements)) {

      List<String> predicates = new ArrayList<>();
      for (FilterElement element : elements) {
        predicates.add(element.getPredicate());
      }

      return "(" + String.join(type == FilterType.AND ? " AND " : " OR ", predicates) + ")";

    } else {
      return getPredicate(QueryUtility.getClassAlias(model) + "." + key);
    }
  }

  private Predicate getNestedPredicate(CriteriaBuilder criteriaBuilder, CommonAbstractCriteria criteria) {

    List<Predicate> predicates = new ArrayList<>();
    for (FilterElement element : this.elements) {
      predicates.add(element.getNestedPredicate(criteriaBuilder, criteria));
    }

    if (type == FilterType.AND) {
      return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
    } else {
      return criteriaBuilder.or(predicates.toArray(new Predicate[]{}));
    }
  }

  private Predicate getPredicate(CriteriaBuilder criteriaBuilder, Path path) {

    switch (operation) {

      case EQUAL:
      case NOT_EQUAL:

        Predicate predicate;
        if (value == null) {
          predicate = criteriaBuilder.isNull(path);
        } else if (value instanceof List || value.getClass().isArray()) {
          predicate = criteriaBuilder.in(path).in(value);
        } else {
          predicate = criteriaBuilder.equal(path, value);
        }

        if (operation == FilterOperation.NOT_EQUAL) {
          predicate = predicate.not();
        }
        return predicate;

      case GREATER:
        return criteriaBuilder.greaterThan(path, value.toString());

      case GREATER_EQUAL:
        return criteriaBuilder.greaterThanOrEqualTo(path, value.toString());

      case LESSER:
        return criteriaBuilder.lessThan(path, value.toString());

      case LESSER_EQUAL:
        return criteriaBuilder.lessThanOrEqualTo(path, value.toString());

      case LIKE:
        return criteriaBuilder.like(path, value.toString());

      case NOT_LIKE:
        return criteriaBuilder.like(path, value.toString()).not();

      default:
        throw new InvalidFilterException(operation.toString());
    }
  }

  private String getPredicate(String path) {

    switch (operation) {

      case EQUAL:
      case NOT_EQUAL:
        if (value == null) {
          return String.format(" %s %s ", path, operation.getNullOperation());
        } else if (value instanceof List || value.getClass().isArray()) {
          return String.format(" %s %s ('%s') ", path, operation.getListOperation(), String.join("','", (List) value));
        } else {
          return String.format(" %s %s '%s' ", path, operation.getOperation(), value);
        }

      case GREATER:
      case GREATER_EQUAL:
      case LESSER:
      case LESSER_EQUAL:
        if (NumberUtils.isCreatable(value.toString())) {
          return String.format(" %s %s %s ", path, operation.getOperation(), NumberUtils.createNumber(value.toString()));
        } else {
          return String.format(" %s %s '%s' ", path, operation.getOperation(), value);
        }

      case LIKE:
      case NOT_LIKE:
      case REGEX:
      case NOT_REGEX:
        return String.format(" %s %s '%s' ", path, operation.getOperation(), value.toString());

      default:
        throw new InvalidFilterException(operation.toString());
    }
  }

  @Override
  public String toString() {
    return this.getPredicate();
  }
}
