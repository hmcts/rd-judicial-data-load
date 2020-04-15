-- NB Flyway requires lowercase for table names
create schema if not exists dbjuddata;

CREATE TABLE dbjuddata.judicial_user_profile(
	elinks_id varchar(256) NOT NULL,
	personal_code varchar(32) NOT NULL,
	title varchar(64) NOT NULL,
	known_as varchar(64) NOT NULL,
	surname varchar(256) NOT NULL,
	full_name varchar(256) NOT NULL,
	post_nominals varchar(64),
	contract_type varchar(32) NOT NULL,
	work_pattern varchar(64) NOT NULL,
	email_id varchar(256) NOT NULL,
	joining_date date,
	last_working_date date,
	active_flag boolean,
	extracted_date timestamp NOT NULL,
	created_date timestamp,
	last_loaded_date timestamp,
	CONSTRAINT personal_code_unique UNIQUE (personal_code),
	CONSTRAINT email_id UNIQUE (email_id),
	CONSTRAINT elinks_id PRIMARY KEY (elinks_id)

);

CREATE TABLE dbjuddata.judicial_office_appointment(
	judicial_office_appointment_id bigint NOT NULL,
	elinks_id varchar(256) NOT NULL,
	role_id varchar(128) NOT NULL,
	contract_type_id varchar(256),
	base_location_id varchar(256),
	region_id varchar(256),
	is_prinicple_appointment boolean,
	start_date date,
	end_date date,
	active_flag boolean,
	extracted_date timestamp NOT NULL,
	created_date timestamp,
	last_loaded_date timestamp,
	CONSTRAINT judicial_office_appointment_id PRIMARY KEY (judicial_office_appointment_id)
);

CREATE TABLE dbjuddata.judicial_office_authorisation(
	judicial_office_auth_id bigint NOT NULL,
	elinks_id varchar(256) NOT NULL,
	authorisation_id varchar(256),
	jurisdiction_id varchar(256) NOT NULL,
	authorisation_date date,
	extracted_date date NOT NULL,
	created_date timestamp,
	last_loaded_date timestamp,
	CONSTRAINT jud_auth_pk PRIMARY KEY (judicial_office_auth_id),
	CONSTRAINT jud_auth_jur_unique UNIQUE (jurisdiction_id)

);
CREATE TABLE dbjuddata.authorisation_type(
	authorisation_id varchar(64) NOT NULL,
	authorisation_desc_en varchar(256) NOT NULL,
	authorisation_desc_cy varchar(256),
	jurisdiction_id varchar(64),
	jurisdiction_desc_en varchar(256),
	jurisdiction_desc_cy varchar(256),
	CONSTRAINT authorisation_id PRIMARY KEY (authorisation_id)

);

CREATE TABLE dbjuddata.judicial_role_type(
	role_id varchar(64) NOT NULL,
	role_desc_en varchar(256) NOT NULL,
	role_desc_cy varchar(256),
	CONSTRAINT role_id PRIMARY KEY (role_id)

);

CREATE TABLE contract_type(
	contract_type_id varchar(64) NOT NULL,
	contract_type_desc_en varchar(256) NOT NULL,
	contract_type_desc_cy varchar(256),
	CONSTRAINT contract_type_id PRIMARY KEY (contract_type_id)

);

CREATE TABLE dbjuddata.base_location_type(
	base_location_id varchar(64) NOT NULL,
	court_name varchar(128),
	bench varchar(128),
	court_type varchar(128),
	circuit varchar(128),
	area_of_expertise varchar(128),
	national_court_code varchar(128),
	CONSTRAINT base_location_id PRIMARY KEY (base_location_id)

);

CREATE TABLE dbjuddata.region_type(
	region_id varchar(64) NOT NULL,
	region_desc_en varchar(256) NOT NULL,
	region_desc_cy varchar(256),
	CONSTRAINT region_id PRIMARY KEY (region_id)

);



ALTER TABLE dbjuddata.judicial_office_appointment ADD CONSTRAINT elinks_Id_fk1 FOREIGN KEY (elinks_Id)
REFERENCES judicial_user_profile (elinks_Id);

ALTER TABLE dbjuddata.judicial_office_appointment ADD CONSTRAINT role_id_fk1 FOREIGN KEY (role_id)
REFERENCES judicial_role_type (role_id);

ALTER TABLE dbjuddata.judicial_office_appointment ADD CONSTRAINT contract_type_Id_fk1 FOREIGN KEY (contract_type_Id)
REFERENCES contract_type (contract_type_Id);

ALTER TABLE dbjuddata.judicial_office_appointment ADD CONSTRAINT base_location_Id_fk1 FOREIGN KEY (base_location_Id)
REFERENCES base_location_type (base_location_Id);

ALTER TABLE dbjuddata.judicial_office_appointment ADD CONSTRAINT region_Id_fk1 FOREIGN KEY (region_Id)
REFERENCES region_type (region_Id);

ALTER TABLE dbjuddata.judicial_office_authorisation ADD CONSTRAINT elinks_Id_fk2 FOREIGN KEY (elinks_Id)
REFERENCES judicial_user_profile (elinks_Id);

ALTER TABLE dbjuddata.judicial_office_authorisation ADD CONSTRAINT authorisation_Id_fk1 FOREIGN KEY (authorisation_Id)
REFERENCES authorisation_type (authorisation_Id);

