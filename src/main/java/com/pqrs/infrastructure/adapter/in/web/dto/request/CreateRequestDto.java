package com.pqrs.infrastructure.adapter.in.web.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateRequestDto(
        @NotBlank(message = "Citizen name is required")
        @Size(min = 3, max = 150, message = "Citizen name must be at least 3 characters and must not exceed 150")
        @JsonProperty("citizen_name")
        String citizenName,
        @NotBlank(message = "Citizen document is required")
        @Size(min = 5, max = 20, message = "Citizen document must be at least 5 characters and must not exceed 20")
        @JsonProperty("citizen_document")
        String citizenDocument,
        @NotBlank(message = "Dependency is required")
        @Size(min = 3, max = 50, message = "Dependency must be at least 3 characters and must not exceed 50")
        String dependency,
        @NotBlank(message = "Dependency is required")
        @Size(min = 10, max = 600, message = "Description must be at least 10 characters and must not exceed 600")
        String description
) {
}
