package com.exatask.platform.mariadb;

import com.exatask.platform.utilities.DateTimeUtility;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@MappedSuperclass
public abstract class AppModel implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  protected Integer id;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtility.LOCAL_ISO_DATE_TIME)
  @CreationTimestamp
  @Column(name = "created_at")
  protected LocalDateTime createdAt;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtility.LOCAL_ISO_DATE_TIME)
  @UpdateTimestamp
  @Column(name = "updated_at")
  protected LocalDateTime updatedAt;
}
