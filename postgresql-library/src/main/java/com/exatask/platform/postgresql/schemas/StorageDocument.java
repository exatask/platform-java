package com.exatask.platform.postgresql.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class StorageDocument {

  @NotEmpty
  @JsonProperty("file")
  private String file;

  @NotEmpty
  @JsonProperty("name")
  private String name;
}
