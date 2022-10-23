package com.exatask.platform.mysql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class AppModel {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  protected Integer id;

  @CreationTimestamp
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Column(name = "created_at")
  protected Date createdAt;

  @UpdateTimestamp
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Column(name = "updated_at")
  protected Date updatedAt;
}
