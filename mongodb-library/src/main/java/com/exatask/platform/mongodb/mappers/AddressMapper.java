package com.exatask.platform.mongodb.mappers;

import com.exatask.platform.dto.entities.AddressEntity;
import com.exatask.platform.mongodb.schemas.Address;
import org.mapstruct.Mapper;

@Mapper(
    componentModel = "spring",
    uses = {AddressMapper.LocationMapper.class}
)
public interface AddressMapper {

  AddressEntity toEntity(Address address);

  @Mapper(componentModel = "spring")
  interface LocationMapper {

    AddressEntity.Location toEntity(Address.Location location);
  }
}
