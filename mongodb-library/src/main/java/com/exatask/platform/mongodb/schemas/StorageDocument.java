package com.exatask.platform.mongodb.schemas;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.NotBlank;

@Data
@Builder
public class StorageDocument {

  @NotBlank(message = "{validations.storage-document.file.not-blank}")
  @Field("file")
  private String file;

  @NotBlank(message = "{validations.storage-document.name.not-blank}")
  @Field("name")
  private String name;
}
