package com.pqrs.application.port.in;

import com.pqrs.domain.model.Request;

import java.util.List;

public interface GetRequestsByUserDependencyUseCase {
    List<Request> execute(String dependency);
}
