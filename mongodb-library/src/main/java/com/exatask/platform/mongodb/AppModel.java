package com.exatask.platform.mongodb;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public abstract class AppModel implements Serializable {

  @Id
  @Field("_id")
  protected ObjectId id;

  @CreatedDate
  @Field("created_at")
  protected LocalDateTime createdAt;

  @LastModifiedDate
  @Field("updated_at")
  protected LocalDateTime updatedAt;
}
