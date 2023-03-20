package com.exatask.platform.mongodb.schemas;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
public class StorageDocument {

  @NotEmpty
  @Field("file")
  private String file;

  @NotEmpty
  @Field("name")
  private String name;
}
