package com.exatask.platform.mongodb.schemas.mappers;

import com.exatask.platform.dto.entities.FullDateEntity;
import com.exatask.platform.mongodb.schemas.FullDate;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FullDateMapper {

  FullDateEntity toEntity(FullDate fullDate);
}
