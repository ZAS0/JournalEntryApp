package com.zeeecom.journalEntry.Mappers;

import com.zeeecom.journalEntry.DTOs.JournalEntryDto;
import com.zeeecom.journalEntry.DTOs.JournalEntryRequestDto;
import com.zeeecom.journalEntry.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface JournalEntryMapper {

    @Mapping(source = "id", target = "id", qualifiedByName = "objectIdToString")
    JournalEntryDto toDTO(JournalEntry entry);

    @Mapping(source = "id", target = "id", qualifiedByName = "stringToObjectId")
    JournalEntry toEntity(JournalEntryDto dto);

    JournalEntry toEntity(JournalEntryRequestDto dto);

    @Named("objectIdToString")
    default String objectIdToString(ObjectId id) {
        return id != null ? id.toString() : null;
    }

    @Named("stringToObjectId")
    default ObjectId stringToObjectId(String id) {
        return (id != null && !id.isBlank()) ? new ObjectId(id) : null;
    }
}
