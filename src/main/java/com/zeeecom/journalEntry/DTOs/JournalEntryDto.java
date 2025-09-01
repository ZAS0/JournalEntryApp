package com.zeeecom.journalEntry.DTOs;

import com.zeeecom.journalEntry.Enums.Sentiment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

import java.time.LocalDateTime;

@Schema(description = "Represents a journal entry returned in API responses")
public record JournalEntryDto(

        @Schema(description = "Unique journal entry ID", example = "64d8bf3e2a1e4b0b0848b7f5")
        String id,

        @Schema(description = "Title of the journal", example = "My Beach Day")
        String name,

        @Schema(description = "Content of the journal", example = "It was sunny and relaxing...")
        String content,

        @Schema(description = "Date and time of the journal entry creation")
        LocalDateTime date,

        @Schema(description = "Sentiment of the journal entry", example = "HAPPY")
        Sentiment sentiment
) {}
