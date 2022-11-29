create schema if not exists dbjudicialdata;

-- dbjudicialdata.judicial_location_mapping definition
CREATE TABLE dbjudicialdata.judicial_location_mapping (
	epimms_id varchar(16),
	judicial_base_location_id varchar(64),
	building_location_name varchar(256),
	base_location_name varchar(128),
	service_code varchar(16)
);


-- dbjudicialdata.jrd_lrd_region_mapping definition
CREATE TABLE dbjudicialdata.jrd_lrd_region_mapping (
	jrd_region_id varchar(16) NOT NULL,
	jrd_region varchar(256) NOT NULL,
	region_id varchar(16) NOT NULL,
	region varchar(256) NOT NULL
);


-- dbjudicialdata.judicial_service_code_mapping definition
CREATE TABLE dbjudicialdata.judicial_service_code_mapping (
	service_id bigint NOT NULL,
	ticket_code varchar(16) NOT NULL,
	service_code varchar(64),
	service_description varchar(512),
	CONSTRAINT service_id PRIMARY KEY (service_id)
);


-- dbjudicialdata.judicial_ticket_code_mapping definition
CREATE TABLE dbjudicialdata.judicial_ticket_code_mapping (
	ticket_code varchar(16) NOT NULL,
	jurisdiction varchar(256) NOT NULL,
	lower_level varchar(256) NOT NULL
);


-- dbjudicialdata.dataload_schedular_audit definition
CREATE TABLE dbjudicialdata.dataload_schedular_audit (
	id serial NOT NULL,
	scheduler_name varchar(64) NOT NULL,
	scheduler_start_time timestamp NOT NULL,
	scheduler_end_time timestamp,
	status varchar(32),
	file_name varchar(128),
	CONSTRAINT dataload_schedular_audit_pk PRIMARY KEY (id)
);


-- dbjudicialdata.dataload_exception_records definition
CREATE TABLE dbjudicialdata.dataload_exception_records (
	id serial NOT NULL,
	table_name varchar(64),
	scheduler_start_time timestamp NOT NULL,
	scheduler_name varchar(64) NOT NULL,
	"key" varchar(256),
	field_in_error varchar(256),
	error_description varchar(512),
	updated_timestamp timestamp NOT NULL,
	row_id int8 NULL,
	CONSTRAINT dataload_exception_records_pk PRIMARY KEY (id)
);


COMMIT;