package com.zeeecom.journalEntry.Mappers;

import com.zeeecom.journalEntry.DTOs.UserDto;
import com.zeeecom.journalEntry.entity.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toDTO(Users user);

    Users toEntity(UserDto dto);

}

