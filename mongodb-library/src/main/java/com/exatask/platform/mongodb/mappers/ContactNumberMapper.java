package com.exatask.platform.mongodb.mappers;

import com.exatask.platform.dto.entities.ContactNumberEntity;
import com.exatask.platform.mongodb.schemas.ContactNumber;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContactNumberMapper {

  ContactNumberEntity toEntity(ContactNumber contactNumber);
}
