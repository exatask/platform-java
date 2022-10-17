package com.exatask.platform.mysql.joins;

import com.exatask.platform.mysql.filters.FilterElement;
import lombok.AllArgsConstructor;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class JoinElement {

  private final String key;

  private final JoinType type;

  private final List<FilterElement> filters;

  public void getJoin(CriteriaBuilder criteriaBuilder, Root from) {

    Join join = from.join(key, type);
    if (CollectionUtils.isEmpty(filters)) {
      return;
    }

    List<Predicate> predicates = new ArrayList<>();
    for (FilterElement filterElement : filters) {
      predicates.add(filterElement.getPredicate(criteriaBuilder, from));
    }

    join.on(predicates.toArray(new Predicate[]{}));
  }
}
