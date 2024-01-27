package com.exatask.platform.mysql.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Builder
public class StorageDocument implements Serializable {

  @NotBlank(message = "{validations.storage-document.file.not-blank}")
  @JsonProperty("file")
  private String file;

  @NotBlank(message = "{validations.storage-document.name.not-blank}")
  @JsonProperty("name")
  private String name;
}
