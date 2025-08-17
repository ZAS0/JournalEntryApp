package com.zeeecom.journalEntry.Mappers;

import com.zeeecom.journalEntry.DTOs.JournalEntryDto;
import com.zeeecom.journalEntry.DTOs.JournalEntryRequestDto;
import com.zeeecom.journalEntry.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface JournalEntryMapper {

    JournalEntryDto toDTO(JournalEntry entry);

    JournalEntry toEntity(JournalEntryDto dto);

    // For request DTO, ignore 'id' (only set name/content/sentiment)
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