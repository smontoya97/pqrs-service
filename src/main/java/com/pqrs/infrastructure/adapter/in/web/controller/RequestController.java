package com.pqrs.infrastructure.adapter.in.web.controller;

import com.pqrs.application.port.in.CreateRequestUseCase;
import com.pqrs.application.port.in.GetRequestsByUserDependencyUseCase;
import com.pqrs.domain.Request;
import com.pqrs.infrastructure.adapter.in.web.dto.request.CreateRequestDto;
import com.pqrs.infrastructure.adapter.in.web.dto.response.RequestResponseDto;
import com.pqrs.infrastructure.adapter.in.web.mapper.WebMapper;
import com.pqrs.infrastructure.config.security.AuthenticatedUser;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/requests")
@AllArgsConstructor
public class RequestController {

    private final CreateRequestUseCase createRequestUseCase;
    private final GetRequestsByUserDependencyUseCase getRequestsByUserDependencyUseCase;

    @PostMapping
    public ResponseEntity<RequestResponseDto> createRequest(@Valid @RequestBody CreateRequestDto createRequestDto) {
        Request request = createRequestUseCase.execute(WebMapper.toCommand(createRequestDto));
        return ResponseEntity.ok(WebMapper.toDto(request));
    }

    @GetMapping
    public ResponseEntity<List<RequestResponseDto>> getRequestsByUserDependency() {
        AuthenticatedUser user = (AuthenticatedUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        List<RequestResponseDto> result = getRequestsByUserDependencyUseCase.execute(user.dependency()).stream()
                .map(WebMapper::toDto)
                .toList();

        return ResponseEntity.ok(result);
    }
}
