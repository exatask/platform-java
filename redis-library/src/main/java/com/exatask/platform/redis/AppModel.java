package com.exatask.platform.redis;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class AppModel {

  @Id
  private String id;
}
