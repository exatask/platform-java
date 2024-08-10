package com.exatask.platform.jpa.queries.sorts;

import com.exatask.platform.jpa.AppModel;
import com.exatask.platform.jpa.utilities.QueryUtility;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;

@AllArgsConstructor
public class SortElement {

  private final Class<? extends AppModel> model;

  private final String key;

  private final Sort.Direction sort;

  public Order getOrder(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery) {

    Path path = criteriaQuery.from(model).get(key);
    return sort == Sort.Direction.ASC ? criteriaBuilder.asc(path) : criteriaBuilder.desc(path);
  }

  public String getOrder() {

    String direction = sort == Sort.Direction.ASC ? "ASC" : "DESC";
    return String.format(" %s.%s %s ", QueryUtility.getClassAlias(model), key, direction);
  }
}
