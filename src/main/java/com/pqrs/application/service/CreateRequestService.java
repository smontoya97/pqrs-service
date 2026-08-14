package com.pqrs.application.service;

import com.pqrs.application.command.CreateRequestCommand;
import com.pqrs.application.port.in.CreateRequestUseCase;
import com.pqrs.application.port.out.RequestRepositoryPort;
import com.pqrs.domain.Request;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateRequestService implements CreateRequestUseCase {

    private final RequestRepositoryPort requestRepositoryPort;

    @Override
    public Request execute(CreateRequestCommand command) {
        return null;
    }
}
