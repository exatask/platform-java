package com.exatask.platform.mongodb.queries.updates;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Update;

@AllArgsConstructor
public class UpdateElement {

  private final String key;

  private final UpdateOperation operation;

  private final Object value;

  public void setUpdate(Update update) {

    switch (operation) {

      case SET:
        update.set(key, value);
        break;

      case UNSET:
        update.unset(key);
        break;

      case ADD_TO_SET:
        update.addToSet(key, value);
        break;

      case INC:
        update.inc(key, Integer.parseInt(value.toString()));
        break;

      case DEC:
        update.inc(key, Integer.parseInt(value.toString()) * -1);
        break;

      case PUSH:
        update.push(key, value);
        break;

      case SET_ON_INSERT:
        update.setOnInsert(key, value);
    }
  }
}
