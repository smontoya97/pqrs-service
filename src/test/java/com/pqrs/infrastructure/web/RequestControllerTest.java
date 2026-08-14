package com.pqrs.infrastructure.web;

import com.pqrs.application.port.in.CreateRequestUseCase;
import com.pqrs.application.port.in.GetRequestsByUserDependencyUseCase;
import com.pqrs.domain.model.Request;
import com.pqrs.domain.model.RequestStatus;
import com.pqrs.infrastructure.adapter.in.web.controller.RequestController;
import com.pqrs.infrastructure.adapter.in.web.dto.request.CreateRequestDto;
import com.pqrs.infrastructure.config.security.AuthenticatedUser;
import com.pqrs.infrastructure.config.security.JwtAuthenticationFilter;
import com.pqrs.infrastructure.config.security.JwtService;
import com.pqrs.infrastructure.config.security.SecurityConfig;
import com.pqrs.infrastructure.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RequestController.class)
@Import({SecurityConfig.class, JwtAuthenticationFilter.class, GlobalExceptionHandler.class})
class RequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateRequestUseCase createRequestUseCase;

    @MockitoBean
    private GetRequestsByUserDependencyUseCase getRequestsByUserDependencyUseCase;

    @MockitoBean
    private JwtService jwtService;

    private UsernamePasswordAuthenticationToken createAuthToken(String username, String dependency, String role) {
        AuthenticatedUser user = new AuthenticatedUser(username, dependency, List.of(role));
        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
        );
    }

    @Test
    @DisplayName("GET /requests - Should return 401 Unauthorized when request is unauthenticated")
    void getRequests_unauthorized_shouldReturn401() throws Exception {
        mockMvc.perform(get("/requests")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        verifyNoInteractions(getRequestsByUserDependencyUseCase);
    }

    @Test
    @DisplayName("GET /requests - Should return 403 Forbidden when authenticated user lacks OFFICIAL role")
    void getRequests_forbidden_shouldReturn403() throws Exception {
        UsernamePasswordAuthenticationToken auth = createAuthToken("citizen_user", "Sistemas", "CITIZEN");

        mockMvc.perform(get("/requests")
                        .with(authentication(auth))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        verifyNoInteractions(getRequestsByUserDependencyUseCase);
    }

    @Test
    @DisplayName("GET /requests - Should return 200 OK and requests list when user has OFFICIAL role")
    void getRequests_authorized_shouldReturn200AndList() throws Exception {
        String dependency = "Recursos Humanos";
        UsernamePasswordAuthenticationToken auth = createAuthToken("official_user", dependency, "OFFICIAL");

        UUID requestId = UUID.randomUUID();
        Request request = Request.reconstitute(
                requestId,
                "Juan Perez",
                "123456789",
                dependency,
                RequestStatus.RECEIVED,
                "Solicitud de certificado laboral",
                LocalDateTime.now()
        );

        when(getRequestsByUserDependencyUseCase.execute(dependency))
                .thenReturn(List.of(request));

        mockMvc.perform(get("/requests")
                        .with(authentication(auth))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(requestId.toString()))
                .andExpect(jsonPath("$[0].citizen_name").value("Juan Perez"))
                .andExpect(jsonPath("$[0].citizen_document").value("123456789"))
                .andExpect(jsonPath("$[0].dependency").value(dependency))
                .andExpect(jsonPath("$[0].status").value("RECEIVED"));

        verify(getRequestsByUserDependencyUseCase).execute(dependency);
    }

    @Test
    @DisplayName("POST /requests - Should return 200 OK and created request when input data is valid")
    void createRequest_success_shouldReturn200() throws Exception {
        CreateRequestDto dto = new CreateRequestDto(
                "Maria Gomez",
                "987654321",
                "Atencion Al Cliente",
                "Queja formal sobre el servicio de atención telefónica"
        );

        UUID requestId = UUID.randomUUID();
        Request request = Request.reconstitute(
                requestId,
                dto.citizenName(),
                dto.citizenDocument(),
                dto.dependency(),
                RequestStatus.RECEIVED,
                dto.description(),
                LocalDateTime.now()
        );

        when(createRequestUseCase.execute(any())).thenReturn(request);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(requestId.toString()))
                .andExpect(jsonPath("$.citizen_name").value("Maria Gomez"))
                .andExpect(jsonPath("$.citizen_document").value("987654321"))
                .andExpect(jsonPath("$.dependency").value("Atencion Al Cliente"))
                .andExpect(jsonPath("$.description").value("Queja formal sobre el servicio de atención telefónica"))
                .andExpect(jsonPath("$.status").value("RECEIVED"));

        verify(createRequestUseCase).execute(any());
    }

    @Test
    @DisplayName("POST /requests - Should return 400 Bad Request when request body fails validation")
    void createRequest_invalidData_shouldReturn400() throws Exception {
        CreateRequestDto invalidDto = new CreateRequestDto(
                "",
                "123",
                "",
                "Corta"
        );

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.details").isArray());

        verifyNoInteractions(createRequestUseCase);
    }
}
