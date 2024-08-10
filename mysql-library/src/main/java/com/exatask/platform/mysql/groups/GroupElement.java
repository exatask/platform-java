package com.exatask.platform.mysql.groups;

import com.exatask.platform.mysql.AppModel;
import com.exatask.platform.mysql.utilities.QueryUtility;
import lombok.AllArgsConstructor;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;

@AllArgsConstructor
public class GroupElement {

  private final Class<? extends AppModel> model;

  private final String key;

  public Expression getGroup(CriteriaQuery criteriaQuery) {
    return criteriaQuery.from(model).get(key);
  }

  public String getGroup() {
    return String.format(" %s.%s ", QueryUtility.getClassAlias(model), key);
  }
}
