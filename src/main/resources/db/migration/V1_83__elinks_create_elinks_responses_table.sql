-- dbjudicialdata.elinks_responses definition
CREATE TABLE dbjudicialdata.elinks_responses(
    id serial  NOT NULL,
    api_name VARCHAR(64) NOT NULL,
    elinks_data JSONB NOT NULL,
    created_date TIMESTAMP NOT NULL,
    CONSTRAINT elinks_responses_pk PRIMARY KEY (id)
);

COMMIT;
