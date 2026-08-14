package com.pqrs.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

public class Request {
    private UUID id;
    private String citizenName;
    private String citizenDocument;
    private String dependency;
    private String description;
    private RequestStatus status;
    private LocalDateTime createdDate;

    private Request(UUID id, String citizenName, String citizenDocument, String dependency, RequestStatus status, String description, LocalDateTime createdDate) {
        validateData(citizenName, citizenDocument, dependency, description);

        this.id = id;
        this.citizenName = citizenName;
        this.citizenDocument = citizenDocument;
        this.dependency = dependency;
        this.status = status;
        this.description = description;
        this.createdDate = createdDate;
    }

    public static Request create(String citizenName, String citizenDocument, String dependency, RequestStatus status, String description) {
        return new Request(
                UUID.randomUUID(),
                citizenName,
                citizenDocument,
                dependency,
                status,
                description,
                LocalDateTime.now(ZoneId.systemDefault())
        );
    }

    public static Request reconstitute(UUID id, String citizenName, String citizenDocument, String dependency, RequestStatus status, String description, LocalDateTime createdDate) {
        return new Request(
                id,
                citizenName,
                citizenDocument,
                dependency,
                status,
                description,
                createdDate
        );
    }

    public UUID getId() {
        return id;
    }

    public String getCitizenName() {
        return citizenName;
    }

    public String getCitizenDocument() {
        return citizenDocument;
    }

    public String getDependency() {
        return dependency;
    }

    public String getDescription() {
        return description;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    private void validateData(String citizenName, String citizenDocument, String dependency, String description) {
        if (citizenName == null || citizenName.isBlank()) {
            throw new IllegalArgumentException("The citizen's name is required");
        }
        if (citizenDocument == null || citizenDocument.isBlank()) {
            throw new IllegalArgumentException("The citizen's document is mandatory");
        }
        if (dependency == null || dependency.isBlank()) {
            throw new IllegalArgumentException("Dependency is mandatory");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("The description is required.");
        }
        if (description.length() > 2000) {
            throw new IllegalArgumentException("The description cannot exceed 2000 characters");
        }
    }
}
