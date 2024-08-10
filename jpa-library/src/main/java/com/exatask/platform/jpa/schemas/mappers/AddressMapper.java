package com.exatask.platform.jpa.schemas.mappers;

import com.exatask.platform.dto.entities.AddressEntity;
import com.exatask.platform.jpa.schemas.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", implementationName = "AddressJpaMapper")
public interface AddressMapper {

  AddressEntity toEntity(Address address);
}
