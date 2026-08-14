package com.pqrs.infrastructure.adapter.in.web.mapper;

import com.pqrs.application.command.CreateRequestCommand;
import com.pqrs.domain.model.Request;
import com.pqrs.infrastructure.adapter.in.web.dto.request.CreateRequestDto;
import com.pqrs.infrastructure.adapter.in.web.dto.response.RequestResponseDto;

public class WebMapper {

    private WebMapper() {
    }

    public static CreateRequestCommand toCommand(CreateRequestDto dto) {
        return new CreateRequestCommand(
                dto.citizenName(),
                dto.citizenDocument(),
                dto.dependency(),
                dto.description()
        );
    }

    public static RequestResponseDto toDto(Request request) {
        return new RequestResponseDto(
                request.getId(),
                request.getCitizenName(),
                request.getCitizenDocument(),
                request.getDependency(),
                request.getDescription(),
                request.getStatus(),
                request.getCreatedDate()
        );
    }
}
