CREATE TABLE judicial_role_type(
	role_id SERIAL,
	per_Id varchar(256) NOT NULL,
	title varchar(256),
	location varchar(256),
	start_date timestamp,
	end_date timestamp,

	CONSTRAINT role_id PRIMARY KEY (role_id)

);

--Modification for US3370
ALTER TABLE judicial_role_type ADD CONSTRAINT per_Id_fk FOREIGN KEY (per_Id)
REFERENCES judicial_user_profile (per_Id);


--Update the Service_Code field length from varchar (16) to Varchar (64)

ALTER TABLE judicial_office_appointment ALTER COLUMN service_code TYPE varchar(64);
ALTER TABLE judicial_service_code_mapping ALTER COLUMN service_code TYPE varchar(64);

--Alter judicial_office_appointment

ALTER TABLE judicial_office_appointment ADD COLUMN appointment varchar(64);
ALTER TABLE judicial_office_appointment ADD COLUMN appointment_type varchar(32);

--Remove 'Appointment' & 'Appointment_Type' fields from judicial_user_profile table
ALTER TABLE judicial_user_profile DROP COLUMN appointment;
ALTER TABLE judicial_user_profile DROP COLUMN appointment_type;
