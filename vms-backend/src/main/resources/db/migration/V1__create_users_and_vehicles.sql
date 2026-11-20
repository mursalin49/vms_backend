CREATE TABLE app_users (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    email         VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    created_at    DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    CONSTRAINT uk_app_users_email UNIQUE (email)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE TABLE vehicles (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    vehicle_number VARCHAR(50)  NOT NULL,
    company        VARCHAR(100) NOT NULL,
    type           VARCHAR(50)  NOT NULL,
    brand          VARCHAR(100) NOT NULL,
    model          VARCHAR(100) NOT NULL,
    status         VARCHAR(20)  NOT NULL,
    created_at     DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    updated_at     DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    PRIMARY KEY (id),
    -- Duplicate Vehicle Number rule, enforced by the database (the collation is case-insensitive)
    CONSTRAINT uk_vehicles_vehicle_number UNIQUE (vehicle_number),
    CONSTRAINT ck_vehicles_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'MAINTENANCE'))
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_unicode_ci;

CREATE INDEX idx_vehicles_status ON vehicles (status);
