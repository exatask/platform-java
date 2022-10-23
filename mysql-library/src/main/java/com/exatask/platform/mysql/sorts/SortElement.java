package com.exatask.platform.mysql.sorts;

import com.exatask.platform.mysql.AppModel;
import com.exatask.platform.mysql.utilities.QueryUtility;
import lombok.AllArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;

@AllArgsConstructor
public class SortElement {

  private final Class<? extends AppModel> model;

  private final String key;

  private final Integer sort;

  public Order getOrder(CriteriaBuilder criteriaBuilder, CriteriaQuery criteriaQuery) {

    Path path = criteriaQuery.from(model).get(key);
    return sort == 1 ? criteriaBuilder.asc(path) : criteriaBuilder.desc(path);
  }

  public String getOrder() {

    String direction = sort == 1 ? "ASC" : "DESC";
    return QueryUtility.getClassAlias(model) + "." + key + " " + direction;
  }
}
