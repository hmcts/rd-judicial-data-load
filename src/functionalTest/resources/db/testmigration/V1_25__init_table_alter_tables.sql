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
ALTER TABLE judicial_office_authorisation ALTER COLUMN service_code TYPE varchar(64);
ALTER TABLE judicial_service_code_mapping ALTER COLUMN service_code TYPE varchar(64);

--Alter judicial_office_appointment

ALTER TABLE judicial_office_appointment ADD COLUMN appointment varchar(64);
ALTER TABLE judicial_office_appointment ADD COLUMN appointment_type varchar(32);
--ALTER TABLE judicial_office_appointment ADD COLUMN role_id varchar(64);

-- Add constraint on judicial_office_appointment
ALTER TABLE judicial_office_appointment ALTER COLUMN appointment SET NOT NULL;

--TODO add following constraint after verify
--ALTER TABLE judicial_office_appointment ADD CONSTRAINT role_id_fk1 FOREIGN KEY (role_id)
--REFERENCES judicial_role_type (role_id);

