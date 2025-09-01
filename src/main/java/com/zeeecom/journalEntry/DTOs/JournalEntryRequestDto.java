package com.zeeecom.journalEntry.DTOs;

import com.zeeecom.journalEntry.Enums.Sentiment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

@Schema(description = "Payload for creating or updating a journal entry")
public record JournalEntryRequestDto(

        @NonNull
        @Schema(description = "Title of the journal", example = "My Beach Day", requiredMode = Schema.RequiredMode.REQUIRED)
        String name,

        @NonNull
        @Schema(description = "Content of the journal", example = "It was sunny and relaxing...")
        String content,

        @NonNull
        @Schema(description = "Sentiment of Yours(HAPPY,SAD,ANXIOUS,RELAXED,ANGRY)", example = "HAPPY")
        Sentiment sentiment

) {}
