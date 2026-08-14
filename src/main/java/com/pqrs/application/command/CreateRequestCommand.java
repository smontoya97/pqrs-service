package com.pqrs.application.command;

public record CreateRequestCommand(
        String citizenName,
        String citizenDocument,
        String dependency,
        String description
) {
}
