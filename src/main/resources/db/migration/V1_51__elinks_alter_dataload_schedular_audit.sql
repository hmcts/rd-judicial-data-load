
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

    DROP TABLE dbjudicialdata.dataload_exception_records;

    CREATE TABLE dbjudicialdata.dataload_exception_records (
    	id serial4 NOT NULL,
    	table_name varchar(64) NULL,
    	scheduler_start_time timestamp NOT NULL,
    	scheduler_name varchar(64) NOT NULL,
    	"key" varchar(256) NULL,
    	field_in_error varchar(256) NULL,
    	error_description varchar(512) NULL,
    	updated_timestamp timestamp NOT NULL,
    	row_id int8 NULL,
    	CONSTRAINT dataload_exception_records_pk PRIMARY KEY (id)
);