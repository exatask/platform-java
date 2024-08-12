package com.exatask.platform.mongodb.queries.updates;

import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.query.Update;

@AllArgsConstructor
public class UpdateElement {

  private final String key;

  private final UpdateOperation operation;

  private final Object value;

  public Update setUpdate(Update update) {

    switch (operation) {

      case SET:
        return update.set(key, value);

      case UNSET:
        return update.unset(key);

      case ADD_TO_SET:
        return update.addToSet(key, value);

      case INC:
        return update.inc(key, Integer.parseInt(value.toString()));

      case DEC:
        return update.inc(key, Integer.parseInt(value.toString()) * -1);

      case PUSH:
        return update.push(key, value);

      case SET_ON_INSERT:
        return update.setOnInsert(key, value);
    }

    return update;
  }
}
