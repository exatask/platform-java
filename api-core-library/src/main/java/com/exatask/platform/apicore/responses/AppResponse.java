package com.exatask.platform.apicore.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public abstract class AppResponse {

  @JsonProperty("status")
  protected Boolean status = true;
}
