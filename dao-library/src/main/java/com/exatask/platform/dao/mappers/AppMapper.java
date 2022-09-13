package com.exatask.platform.dao.mappers;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class AppMapper {

  public String uuidToString(UUID uuid) {
    return uuid.toString();
  }
}
