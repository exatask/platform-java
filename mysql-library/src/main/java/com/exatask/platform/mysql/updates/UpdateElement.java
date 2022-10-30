package com.exatask.platform.mysql.updates;

import com.exatask.platform.mysql.AppModel;
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
        criteriaUpdate = criteriaUpdate.set(path, value);
        break;

      case UNSET:
        criteriaUpdate = criteriaUpdate.set(path, criteriaBuilder.nullLiteral(path.getClass()));

      case INC:
        criteriaUpdate = criteriaUpdate.set(path, criteriaBuilder.sum(path, Integer.parseInt(value.toString())));
        break;

      case DEC:
        criteriaUpdate = criteriaUpdate.set(path, criteriaBuilder.sum(path, Integer.parseInt(value.toString()) * -1));
        break;
    }

    return criteriaUpdate;
  }
}
