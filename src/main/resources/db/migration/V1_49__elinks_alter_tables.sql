--Drop tables
DROP TABLE dbjudicialdata.judicial_role_type;
DROP TABLE dbjudicialdata.judicial_office_authorisation;
DROP TABLE dbjudicialdata.judicial_office_appointment;
DROP TABLE dbjudicialdata.judicial_user_profile;



-- Create table script : judicial_user_profile
CREATE TABLE dbjudicialdata.judicial_user_profile (
	personal_code varchar(32) NOT NULL,
	known_as varchar(64) NOT NULL,
	surname varchar(256) NOT NULL,
	full_name varchar(256) NOT NULL,
	post_nominals varchar(64) NULL,
	ejudiciary_email varchar(256) NULL,
	last_working_date date NULL,
	active_flag boolean NULL,
	created_date timestamp NULL,
	last_loaded_date timestamp NULL,
	object_id varchar(64) NULL,
	sidam_id varchar(64) NULL,
	initials varchar(64) NULL,
	CONSTRAINT judicial_user_profile_pkey PRIMARY KEY (personal_code)
);

-- Create table script : judicial_role_type
CREATE TABLE dbjudicialdata.judicial_role_type (
	role_id SERIAL,
	personal_code varchar(32) NOT NULL,
	title varchar(256) NULL,
	start_date timestamp NULL,
	end_date timestamp NULL,
	CONSTRAINT role_id PRIMARY KEY (role_id),
   	CONSTRAINT personal_code_fk FOREIGN KEY (personal_code) REFERENCES dbjudicialdata.judicial_user_profile(personal_code)
);



-- Create table script : judicial_office_authorisation
CREATE TABLE dbjudicialdata.judicial_office_authorisation (
	judicial_office_auth_id bigint NOT NULL,
	personal_code varchar(32) NOT NULL,
	jurisdiction varchar(256) NULL,
	start_date timestamp NULL,
	end_date timestamp NULL,
	created_date timestamp NULL,
	last_updated timestamp NULL,
	lower_level varchar(256) NULL,
	object_id varchar(64) NULL,
	ticket_code varchar(16) NULL,
	CONSTRAINT jud_auth_pk PRIMARY KEY (judicial_office_auth_id),
   	CONSTRAINT personal_code_fk FOREIGN KEY (personal_code) REFERENCES dbjudicialdata.judicial_user_profile(personal_code)

);

-- Create table script : judicial_office_appointment
CREATE TABLE dbjudicialdata.judicial_office_appointment (
	judicial_office_appointment_id bigint NOT NULL,
	personal_code varchar(32) NOT NULL,
	base_location_id varchar(64) NULL,
	region_id varchar(256) NULL,
	is_prinicple_appointment boolean NULL,
	start_date date NULL,
	end_date date NULL,
	created_date timestamp NULL,
	last_loaded_date timestamp NULL,
	epimms_id varchar(16) NULL,
	service_code varchar(64) NULL,
	object_id varchar(64) NULL,
	appointment varchar(64) NULL,
	appointment_type varchar(32) NULL,
	work_pattern varchar(64) NULL,
	CONSTRAINT judicial_office_appointment_id PRIMARY KEY (judicial_office_appointment_id),
   	CONSTRAINT base_location_id_fk1 FOREIGN KEY (base_location_id) REFERENCES dbjudicialdata.base_location_type(base_location_id),
   	CONSTRAINT personal_code_fk FOREIGN KEY (personal_code) REFERENCES dbjudicialdata.judicial_user_profile(personal_code),

   	CONSTRAINT region_id_fk1 FOREIGN KEY (region_id) REFERENCES dbjudicialdata.region_type(region_id)
);