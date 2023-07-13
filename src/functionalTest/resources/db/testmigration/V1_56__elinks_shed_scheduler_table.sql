--Created table for lock details
CREATE TABLE dbjudicialdata.lock_details_provider(name VARCHAR(64) NOT NULL, locked_at TIMESTAMP NOT NULL,
                 lock_until TIMESTAMP NOT NULL, locked_by VARCHAR(255) NOT NULL, PRIMARY KEY (name));