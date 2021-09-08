package com.exatask.platform.redis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

@Data
@NoArgsConstructor
public abstract class AppModel {

  @Id
  @JsonIgnore
  private String id;
}
