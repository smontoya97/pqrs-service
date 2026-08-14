package com.pqrs.infrastructure.adapter.out.persistence.mapper;

import com.pqrs.domain.model.Request;
import com.pqrs.infrastructure.adapter.out.persistence.entity.RequestEntity;

public class RequestMapper {

    private RequestMapper() {}

    public static RequestEntity toEntity(Request request) {
        return new RequestEntity(
                request.getId(),
                request.getCitizenName(),
                request.getCitizenDocument(),
                request.getDependency(),
                request.getDescription(),
                request.getStatus(),
                request.getCreatedDate()
        );
    }

    public static Request toEntity(RequestEntity entity) {
        return Request.reconstitute(
                entity.getId(),
                entity.getCitizenName(),
                entity.getCitizenDocument(),
                entity.getDependency(),
                entity.getStatus(),
                entity.getDescription(),
                entity.getCreatedDate()
        );
    }
}
