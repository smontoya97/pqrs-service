CREATE TABLE IF NOT EXISTS requests (
    id CHAR(36) PRIMARY KEY COMMENT 'UUID identifier',
    citizen_name VARCHAR(255) NOT NULL COMMENT 'Name of the citizen making the request',
    citizen_document VARCHAR(50) NOT NULL COMMENT 'Identification document of the citizen',
    dependency VARCHAR(255) NOT NULL COMMENT 'Department or dependency handling the request',
    description LONGTEXT NOT NULL COMMENT 'Detailed description of the request',
    status VARCHAR(50) NOT NULL COMMENT 'Status of the request (PENDING, IN_PROGRESS, RESOLVED, REJECTED)',
    created_date DATETIME NOT NULL COMMENT 'Date and time when the request was created',

    INDEX idx_citizen_document (citizen_document),
    INDEX idx_dependency (dependency),
    INDEX idx_status (status),
    INDEX idx_created_date (created_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='PQRS Service requests table';


