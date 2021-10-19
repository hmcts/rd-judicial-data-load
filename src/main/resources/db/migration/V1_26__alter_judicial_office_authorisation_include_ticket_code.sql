ALTER TABLE judicial_office_authorisation DROP COLUMN service_code;

ALTER TABLE judicial_office_authorisation ADD COLUMN ticket_code varchar(16);