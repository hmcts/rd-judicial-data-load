ALTER TABLE dbjudicialdata.judicial_office_appointment  DROP CONSTRAINT base_location_id_fk1;

drop table dbjudicialdata.base_location_type;

CREATE TABLE dbjudicialdata.location_type (
	base_location_id varchar(64) NOT NULL,
	name varchar(256) NULL,
	type_id varchar(64) NULL,
	parent_id varchar(64) NULL,
	jurisdiction_id varchar(64) NULL,
	start_date timestamp NULL,
	end_date timestamp NULL,
	created_at timestamp NULL,
	updated_at timestamp NULL,
	CONSTRAINT base_location_id PRIMARY KEY (base_location_id)
);

alter table dbjudicialdata.judicial_office_appointment add CONSTRAINT base_location_id_fk1 FOREIGN KEY (base_location_id) REFERENCES dbjudicialdata.location_type(base_location_id);

ALTER TABLE dbjudicialdata.judicial_office_appointment  DROP CONSTRAINT region_id_fk1;

ALTER TABLE dbjudicialdata.judicial_office_appointment RENAME COLUMN region_id TO cft_region_id;

ALTER TABLE dbjudicialdata.judicial_office_appointment ALTER COLUMN cft_region_id TYPE varchar(64);


drop table dbjudicialdata.region_type ;

CREATE TABLE dbjudicialdata.cft_region_type (
	cft_region_id varchar(64) NOT NULL,
	cft_region_desc_en varchar(256) NOT NULL,
	cft_region_desc_cy varchar(256) NULL,
	CONSTRAINT cft_region_id PRIMARY KEY (cft_region_id)
);

alter table dbjudicialdata.judicial_office_appointment add CONSTRAINT cft_region_id_fk1 FOREIGN KEY (cft_region_id) REFERENCES dbjudicialdata.cft_region_type(cft_region_id);

ALTER TABLE dbjudicialdata.judicial_office_appointment add COLUMN jo_base_location_id varchar(64) NOT null;

alter table dbjudicialdata.judicial_office_appointment add CONSTRAINT jo_base_location_id_fk1 FOREIGN KEY (jo_base_location_id) REFERENCES dbjudicialdata.location_type(base_location_id);

