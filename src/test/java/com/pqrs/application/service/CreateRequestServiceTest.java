package com.pqrs.application.service;

import com.pqrs.application.command.CreateRequestCommand;
import com.pqrs.application.event.RequestCreatedEvent;
import com.pqrs.application.port.out.EventPublisherPort;
import com.pqrs.application.port.out.RequestRepositoryPort;
import com.pqrs.domain.model.Request;
import com.pqrs.domain.model.RequestStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateRequestServiceTest {

    @Mock
    private RequestRepositoryPort requestRepositoryPort;

    @Mock
    private EventPublisherPort eventPublisherPort;

    @InjectMocks
    private CreateRequestService createRequestService;

    @Test
    @DisplayName("Should successfully create request, persist to repository, and publish event")
    void execute_success_shouldSaveAndPublishEvent() {
        CreateRequestCommand command = new CreateRequestCommand(
                "Carlos Mendoza",
                "1020304050",
                "Infraestructura",
                "Petición de reparación de vía pública en el sector norte"
        );

        when(requestRepositoryPort.save(any(Request.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Request result = createRequestService.execute(command);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals("Carlos Mendoza", result.getCitizenName());
        assertEquals("1020304050", result.getCitizenDocument());
        assertEquals("Infraestructura", result.getDependency());
        assertEquals("Petición de reparación de vía pública en el sector norte", result.getDescription());
        assertEquals(RequestStatus.RECEIVED, result.getStatus());
        assertNotNull(result.getCreatedDate());

        verify(requestRepositoryPort).save(any(Request.class));

        ArgumentCaptor<RequestCreatedEvent> eventCaptor = ArgumentCaptor.forClass(RequestCreatedEvent.class);
        verify(eventPublisherPort).publish(eventCaptor.capture());

        RequestCreatedEvent publishedEvent = eventCaptor.getValue();
        assertEquals(result.getId(), publishedEvent.requestId());
        assertEquals("Infraestructura", publishedEvent.dependency());
        assertEquals(result.getCreatedDate(), publishedEvent.occurredAt());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when command contains invalid domain data")
    void execute_invalidDomainData_shouldThrowException() {
        CreateRequestCommand invalidCommand = new CreateRequestCommand(
                "",
                "1020304050",
                "Infraestructura",
                "Petición de reparación"
        );

        assertThrows(IllegalArgumentException.class, () -> createRequestService.execute(invalidCommand));

        verifyNoInteractions(requestRepositoryPort);
        verifyNoInteractions(eventPublisherPort);
    }
}
