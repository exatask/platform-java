package com.exatask.platform.mongodb;

import com.exatask.platform.mongodb.serializers.ObjectIdSerializer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
  @JsonProperty("_id")
  @JsonSerialize(using = ObjectIdSerializer.class)
  @Field("_id")
  protected ObjectId id;

  @CreatedDate
  @JsonProperty("created_at")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Field("created_at")
  protected Date createdAt;

  @LastModifiedDate
  @JsonProperty("updated_at")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Field("updated_at")
  protected Date updatedAt;
}
