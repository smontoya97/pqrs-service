package com.pqrs.infrastructure.adapter.in.web.dto.response;

import com.pqrs.domain.RequestStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record RequestResponseDto(
        UUID id,
        String citizenName,
        String citizenDocument,
        String dependency,
        String description,
        RequestStatus status,
        LocalDateTime createdDate
) {
}
