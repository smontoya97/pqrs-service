package com.pqrs.application.port.in;

import com.pqrs.application.command.CreateRequestCommand;
import com.pqrs.domain.model.Request;

public interface CreateRequestUseCase {
    Request execute(CreateRequestCommand command);
}
