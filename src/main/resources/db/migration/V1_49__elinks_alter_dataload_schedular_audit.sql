
DROP TABLE dbjudicialdata.dataload_schedular_audit;

CREATE TABLE dbjudicialdata.dataload_schedular_audit (
	id serial NOT NULL,
	scheduler_name varchar(64) NOT NULL,
	scheduler_start_time timestamp NOT NULL,
	scheduler_end_time timestamp NULL,
	status varchar(32) NULL,
	api_name varchar(128) NULL,
	CONSTRAINT dataload_schedular_audit_pk PRIMARY KEY (id)
);