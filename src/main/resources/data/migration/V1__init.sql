CREATE TABLE upload_job
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMP   NOT NULL DEFAULT NOW(),
    finished_at TIMESTAMP,
    status      VARCHAR(20) NOT NULL, -- PENDING | RUNNING | DONE | FAILED
    note        TEXT
);

CREATE TABLE validation_result
(
    id          BIGSERIAL PRIMARY KEY,
    job_id      UUID      NOT NULL REFERENCES upload_job (id) ON DELETE CASCADE,
    file_name    TEXT      NOT NULL,
    success     BOOLEAN   NOT NULL,
    message     TEXT,
    errors_json JSONB, -- structured error list
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

-- Optional future: rule sets per tenant
-- CREATE TABLE rule_set ( ... );
/*ctrl+alt+L*/