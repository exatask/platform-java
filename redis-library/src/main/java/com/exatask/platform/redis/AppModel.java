package com.exatask.platform.redis;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Data
@NoArgsConstructor
public abstract class AppModel implements Serializable {

  @Id
  @JsonIgnore
  private String id;
}
