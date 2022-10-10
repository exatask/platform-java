package com.exatask.platform.mysql.updates;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class AppUpdate {

  @Getter
  private final List<UpdateElement> updates;

  public AppUpdate() {
    updates = new ArrayList<>();
  }

  public void addUpdate(String key, Object value) {
    addUpdate(new UpdateElement(key, UpdateOperation.SET, value));
  }

  public void addUpdate(UpdateElement updateElement) {
    updates.add(updateElement);
  }
}
