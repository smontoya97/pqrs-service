package com.pqrs.infrastructure.adapter.out.persistence.entity;

import com.pqrs.domain.RequestStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestEntity {

    @Id
    private UUID id;
    @Column(name = "citizen_name", nullable = false)
    private String citizenName;
    @Column(name = "citizen_document", nullable = false)
    private String citizenDocument;
    @Column(name = "dependency", nullable = false)
    private String dependency;
    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestStatus status;
    @Column(name = "created_date", nullable = false)
    private LocalDateTime createdDate;
}
