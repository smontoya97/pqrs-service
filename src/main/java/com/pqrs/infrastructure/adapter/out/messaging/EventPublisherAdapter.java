package com.pqrs.infrastructure.adapter.out.messaging;

import com.pqrs.application.port.out.EventPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventPublisherAdapter implements EventPublisherPort {

    public static final Logger LOGGER = LoggerFactory.getLogger(EventPublisherAdapter.class);

    @Override
    public void publish(DomainEvent event) {
        LOGGER.info("[Event published]: type={}, payload={}", event.type(), event);
    }
}
