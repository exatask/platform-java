package com.exatask.platform.mysql.updates;

import com.exatask.platform.mysql.AppModel;
import lombok.AllArgsConstructor;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Path;

@AllArgsConstructor
public class UpdateElement {

  private final Class<? extends AppModel> model;

  private final String key;

  private final UpdateOperation mongoOperation;

  private final Object value;

  public CriteriaUpdate setUpdate(CriteriaBuilder criteriaBuilder, CriteriaUpdate criteriaUpdate) {

    Path path = criteriaUpdate.from(model).get(key);

    switch (mongoOperation) {

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
