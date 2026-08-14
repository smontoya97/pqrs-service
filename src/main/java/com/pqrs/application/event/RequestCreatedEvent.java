package com.pqrs.application.event;

import com.pqrs.application.port.out.EventPublisherPort;

import java.time.LocalDateTime;
import java.util.UUID;

public record RequestCreatedEvent(
        UUID requestId,
        String dependency,
        LocalDateTime occurredAt
) implements EventPublisherPort.DomainEvent {
    @Override
    public String type() {
        return "RequestCreatedEvent";
    }
}
