package com.exatask.platform.jpa.queries.updates;

import com.exatask.platform.jpa.AppModel;
import lombok.AllArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

@AllArgsConstructor
public class UpdateElement {

  private final Class<? extends AppModel> model;

  private final String key;

  private final UpdateOperation operation;

  private final Object value;

  public CriteriaUpdate setUpdate(CriteriaBuilder criteriaBuilder, CriteriaUpdate criteriaUpdate) {

    Root from = criteriaUpdate.getRoot();
    if (from.getModel().getJavaType() != model) {
      from = criteriaUpdate.from(model);
    }

    Path path = from.get(key);
    switch (operation) {

      case SET:
        return criteriaUpdate.set(path, value);

      case UNSET:
        return criteriaUpdate.set(path, criteriaBuilder.nullLiteral(path.getClass()));

      case INC:
        return criteriaUpdate.set(path, criteriaBuilder.sum(path, Integer.parseInt(value.toString())));

      case DEC:
        return criteriaUpdate.set(path, criteriaBuilder.sum(path, Integer.parseInt(value.toString()) * -1));
    }

    return criteriaUpdate;
  }
}
