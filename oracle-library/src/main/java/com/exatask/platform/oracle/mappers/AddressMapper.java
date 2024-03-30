package com.exatask.platform.oracle.mappers;

import com.exatask.platform.dto.entities.AddressEntity;
import com.exatask.platform.oracle.schemas.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", implementationName = "AddressOracleMapper")
public interface AddressMapper {

  AddressEntity toEntity(Address address);
}
