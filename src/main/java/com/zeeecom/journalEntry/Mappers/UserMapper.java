package com.zeeecom.journalEntry.Mappers;

import com.zeeecom.journalEntry.DTOs.UserDto;
import com.zeeecom.journalEntry.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "sentimentAnalysis", target = "sentimentAnalysis")
    UserDto toDTO(Users user);

    @Mapping(source = "userName", target = "userName")
    @Mapping(source = "password", target = "password")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "sentimentAnalysis", target = "sentimentAnalysis")
    Users toEntity(UserDto dto);
}
