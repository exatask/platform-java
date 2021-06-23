package com.exatask.platform.mongodb;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@NoArgsConstructor
public abstract class AppModel {

  @Id
  @Setter(value = AccessLevel.NONE)
  @Field("_id")
  protected ObjectId id;

  @CreatedDate
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Field("created_at")
  protected Date createdAt;

  @LastModifiedDate
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Field("updated_at")
  protected Date updatedAt;
}
