package com.pqrs.application.port.out;

import com.pqrs.domain.model.Request;

import java.util.List;

public interface RequestRepositoryPort {
    Request save(Request request);
    List<Request> getByDependency(String dependency);
}
