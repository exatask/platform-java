package com.exatask.platform.mysql.mappers;

import com.exatask.platform.dto.entities.AddressEntity;
import com.exatask.platform.mysql.schemas.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

  AddressEntity toEntity(Address address);
}
