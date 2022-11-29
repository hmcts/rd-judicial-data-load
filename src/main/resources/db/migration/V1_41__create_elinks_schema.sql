-- NB Flyway requires lowercase for table names
CREATE SCHEMA IF NOT EXISTS dbjudicialdata;

-- Create table script : base_location_type
CREATE TABLE dbjudicialdata.base_location_type (
	base_location_id varchar(64) NOT NULL,
	court_name varchar(128) NULL,
	court_type varchar(128) NULL,
	circuit varchar(128) NULL,
	area_of_expertise varchar(128) NULL,
	CONSTRAINT base_location_id PRIMARY KEY (base_location_id)
);

-- Create table script : region_type
CREATE TABLE dbjudicialdata.region_type (
	region_id varchar(64) NOT NULL,
	region_desc_en varchar(256) NOT NULL,
	region_desc_cy varchar(256) NULL,
	CONSTRAINT region_id PRIMARY KEY (region_id)
);

-- Create table script : judicial_user_profile
CREATE TABLE dbjudicialdata.judicial_user_profile (
	per_id varchar(256) NOT NULL,
	personal_code varchar(32) NOT NULL,
	known_as varchar(64) NOT NULL,
	surname varchar(256) NOT NULL,
	full_name varchar(256) NOT NULL,
	post_nominals varchar(64) NULL,
	work_pattern varchar(64) NULL,
	ejudiciary_email varchar(256) NULL,
	joining_date date NULL,
	last_working_date date NULL,
	active_flag boolean NULL,
	extracted_date timestamp NOT NULL,
	created_date timestamp NULL,
	last_loaded_date timestamp NULL,
	object_id varchar(64) NULL,
	sidam_id varchar(64) NULL,
	CONSTRAINT judicial_user_profile_pkey PRIMARY KEY (per_id)
);

-- Create table script : judicial_role_type
CREATE TABLE dbjudicialdata.judicial_role_type (
	role_id SERIAL,
	per_id varchar(256) NOT NULL,
	title varchar(256) NULL,
	location varchar(256) NULL,
	start_date timestamp NULL,
	end_date timestamp NULL,
	CONSTRAINT role_id PRIMARY KEY (role_id),
   	CONSTRAINT per_id_fk FOREIGN KEY (per_id) REFERENCES dbjudicialdata.judicial_user_profile(per_id)
);



-- Create table script : judicial_office_authorisation
CREATE TABLE dbjudicialdata.judicial_office_authorisation (
	judicial_office_auth_id bigint NOT NULL,
	per_id varchar(256) NOT NULL,
	jurisdiction varchar(256) NULL,
	ticket_id bigint NULL,
	start_date timestamp NULL,
	end_date timestamp NULL,
	created_date timestamp NULL,
	last_updated timestamp NULL,
	lower_level varchar(256) NULL,
	personal_code varchar(32) NULL,
	object_id varchar(64) NULL,
	ticket_code varchar(16) NULL,
	CONSTRAINT jud_auth_pk PRIMARY KEY (judicial_office_auth_id),
   	CONSTRAINT per_id_fk FOREIGN KEY (per_id) REFERENCES dbjudicialdata.judicial_user_profile(per_id)
);

-- Create table script : judicial_office_appointment
CREATE TABLE dbjudicialdata.judicial_office_appointment (
	judicial_office_appointment_id bigint NOT NULL,
	per_id varchar(256) NOT NULL,
	base_location_id varchar(64) NULL,
	region_id varchar(256) NULL,
	is_prinicple_appointment boolean NULL,
	start_date date NULL,
	end_date date NULL,
	active_flag boolean NULL,
	extracted_date timestamp NOT NULL,
	created_date timestamp NULL,
	last_loaded_date timestamp NULL,
	personal_code varchar(32) NULL,
	epimms_id varchar(16) NULL,
	service_code varchar(64) NULL,
	object_id varchar(64) NULL,
	appointment varchar(64) NULL,
	appointment_type varchar(32) NULL,
	CONSTRAINT judicial_office_appointment_id PRIMARY KEY (judicial_office_appointment_id),
   	CONSTRAINT base_location_id_fk1 FOREIGN KEY (base_location_id) REFERENCES dbjudicialdata.base_location_type(base_location_id),
   	CONSTRAINT per_id_fk FOREIGN KEY (per_id) REFERENCES dbjudicialdata.judicial_user_profile(per_id),
   	CONSTRAINT region_id_fk1 FOREIGN KEY (region_id) REFERENCES dbjudicialdata.region_type(region_id)
);
