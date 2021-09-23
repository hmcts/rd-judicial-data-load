CREATE TABLE judicial_role_type(
	role_id varchar(64) NOT NULL,
	role_desc_en varchar(256) NOT NULL,
	role_desc_cy varchar(256),
	CONSTRAINT role_id PRIMARY KEY (role_id)

);


--Update the Service_Code field length from varchar (16) to Varchar (64)

ALTER TABLE judicial_office_appointment ALTER COLUMN service_code TYPE varchar(64);
ALTER TABLE judicial_office_authorisation ALTER COLUMN service_code TYPE varchar(64);
ALTER TABLE judicial_service_code_mapping ALTER COLUMN service_code TYPE varchar(64);

--Alter judicial_office_appointment

ALTER TABLE judicial_office_appointment ADD COLUMN appointment varchar(64);
ALTER TABLE judicial_office_appointment ADD COLUMN appointment_type varchar(32);
ALTER TABLE judicial_office_appointment ADD COLUMN role_id varchar(64);
