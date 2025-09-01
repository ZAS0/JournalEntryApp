package com.zeeecom.journalEntry.Mappers;

import com.zeeecom.journalEntry.DTOs.SignupRequestDto;
import com.zeeecom.journalEntry.entity.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PublicMapper {
    Users toEntity(SignupRequestDto dto);
}
