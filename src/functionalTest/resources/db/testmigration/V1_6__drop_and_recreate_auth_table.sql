DROP TABLE judicial_office_authorisation;
DROP TABLE authorisation_type;

CREATE TABLE judicial_office_authorisation(
    judicial_office_auth_id bigint,
    elinks_id varchar(256),
    jurisdiction varchar(256),
    ticket_id bigint,
    start_date timestamp,
	end_date timestamp,
	created_date timestamp,
    last_updated timestamp,
    lower_level varchar(256),
	CONSTRAINT jud_auth_pk PRIMARY KEY (judicial_office_auth_id)
);

ALTER TABLE judicial_office_authorisation ADD CONSTRAINT elinks_id FOREIGN KEY (elinks_id)
REFERENCES judicial_user_profile (elinks_id) MATCH FULL
ON DELETE NO ACTION ON UPDATE NO ACTION;