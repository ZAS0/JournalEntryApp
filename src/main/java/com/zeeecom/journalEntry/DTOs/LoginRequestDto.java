package com.zeeecom.journalEntry.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.NonNull;

@Schema(description = "Login request payload")
public record LoginRequestDto(

        @NonNull
        @Schema(description = "Username", example = "john_doe", requiredMode = Schema.RequiredMode.REQUIRED)
        String userName,

        @NonNull
        @Schema(description = "Password", example = "mypassword", requiredMode = Schema.RequiredMode.REQUIRED)
        String password
) {}
