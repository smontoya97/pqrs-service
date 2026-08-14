package com.pqrs.infrastructure.adapter.in.web.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pqrs.domain.model.RequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record RequestResponseDto(
        UUID id,
        @JsonProperty("citizen_name")
        String citizenName,
        @JsonProperty("citizen_document")
        String citizenDocument,
        String dependency,
        String description,
        RequestStatus status,
        @JsonProperty("created_date")
        LocalDateTime createdDate
) {
}
