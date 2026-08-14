package com.pqrs.infrastructure.adapter.out.persistence.adapter;

import com.pqrs.application.port.out.RequestRepositoryPort;
import com.pqrs.domain.Request;
import com.pqrs.infrastructure.adapter.out.persistence.entity.RequestEntity;
import com.pqrs.infrastructure.adapter.out.persistence.jpa.RequestJpaRepository;
import com.pqrs.infrastructure.adapter.out.persistence.mapper.RequestMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@AllArgsConstructor
public class RequestRepositoryAdapter implements RequestRepositoryPort {

    private final RequestJpaRepository requestJpaRepository;

    @Override
    public Request save(Request request) {
        RequestEntity requestEntity = RequestMapper.toEntity(request);
        RequestEntity saved = requestJpaRepository.save(requestEntity);
        return RequestMapper.toEntity(saved);
    }

    @Override
    public List<Request> getByDependency(String dependency) {
        return requestJpaRepository.findByDependency(dependency).stream()
                .map(RequestMapper::toEntity)
                .toList();
    }
}
