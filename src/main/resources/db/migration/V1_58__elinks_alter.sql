--Alter dbjudicialdata.judicial_user_profile
ALTER TABLE dbjudicialdata.judicial_user_profile ALTER COLUMN post_nominals type VARCHAR(256);
ALTER TABLE dbjudicialdata.judicial_user_profile DROP COLUMN leaving_on;


--Alter dbjudicialdata.judicial_office_appointment
ALTER TABLE dbjudicialdata.judicial_office_appointment   ADD UNIQUE (appointment_id);


--Alter dbjudicialdata.judicial_office_authorisation
ALTER TABLE dbjudicialdata.judicial_office_authorisation ADD CONSTRAINT appointment_id_fk2 FOREIGN KEY (appointment_id)
REFERENCES dbjudicialdata.judicial_office_appointment(appointment_id);