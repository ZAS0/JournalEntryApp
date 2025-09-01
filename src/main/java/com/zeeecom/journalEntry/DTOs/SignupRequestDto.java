package com.zeeecom.journalEntry.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

@Schema(description = "Signup request payload")
public record SignupRequestDto(

        @NonNull
        @Schema(description = "Username", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
        String userName,

        @NonNull
        @Schema(description = "Password", example = "mypassword", requiredMode = Schema.RequiredMode.REQUIRED)
        String password,

        @Schema(description = "Email address", example = "john@example.com")
        String email,

        @Schema(description = "Whether sentiment analysis is enabled", example = "true")
        boolean sentimentAnalysis
) {}
