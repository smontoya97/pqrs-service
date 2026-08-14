package com.pqrs.application.port.out;

public interface EventPublisherPort {
    void publish(DomainEvent event);

    interface DomainEvent {
        String type();
    }
}
