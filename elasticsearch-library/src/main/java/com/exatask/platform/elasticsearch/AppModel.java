package com.exatask.platform.elasticsearch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public abstract class AppModel implements Serializable {

  @Id
  @Field("id")
  protected String id;

  @Field(name = "tenant", type = FieldType.Keyword)
  protected String tenant;

  @CreatedDate
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Field(name = "created_at", type = FieldType.Date, format = DateFormat.date_optional_time)
  protected LocalDateTime createdAt;

  @LastModifiedDate
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Field(name = "updated_at", type = FieldType.Date, format = DateFormat.date_optional_time)
  protected LocalDateTime updatedAt;
}
