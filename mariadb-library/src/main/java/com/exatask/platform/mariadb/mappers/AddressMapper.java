package com.exatask.platform.mariadb.mappers;

import com.exatask.platform.dto.entities.AddressEntity;
import com.exatask.platform.mariadb.schemas.Address;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", implementationName = "AddressMariadbMapper")
public interface AddressMapper {

  AddressEntity toEntity(Address address);
}
