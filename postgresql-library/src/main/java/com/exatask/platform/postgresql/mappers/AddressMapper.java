package com.exatask.platform.postgresql.mappers;

import com.exatask.platform.dto.entities.AddressEntity;
import com.exatask.platform.postgresql.schemas.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

  AddressEntity toEntity(Address address);
}
