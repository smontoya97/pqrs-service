package com.pqrs.application.service;

import com.pqrs.application.port.in.GetRequestsByUserDependencyUseCase;
import com.pqrs.application.port.out.RequestRepositoryPort;
import com.pqrs.domain.Request;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class GetRequestsByUserDependencyService implements GetRequestsByUserDependencyUseCase {

    private final RequestRepositoryPort requestRepositoryPort;

    @Override
    public List<Request> execute() {
        return List.of();
    }
}
