package com.pqrs.application.service;

import com.pqrs.application.command.CreateRequestCommand;
import com.pqrs.application.event.RequestCreatedEvent;
import com.pqrs.application.port.in.CreateRequestUseCase;
import com.pqrs.application.port.out.EventPublisherPort;
import com.pqrs.application.port.out.RequestRepositoryPort;
import com.pqrs.domain.Request;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CreateRequestService implements CreateRequestUseCase {

    private final RequestRepositoryPort requestRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    @Override
    public Request execute(CreateRequestCommand command) {
        Request request = Request.create(
                command.citizenName(),
                command.citizenDocument(),
                command.dependency(),
                command.description()
        );
        Request saved =  requestRepositoryPort.save(request);
        eventPublisherPort.publish(new RequestCreatedEvent(saved.getId(), saved.getDependency(), saved.getCreatedDate()));
        return saved;
    }
}
