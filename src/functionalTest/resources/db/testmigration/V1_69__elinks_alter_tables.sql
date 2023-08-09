--Alter dbjudicialdata.judicial_user_profile
ALTER TABLE dbjudicialdata.judicial_user_profile ADD COLUMN deleted_flag boolean;
ALTER TABLE dbjudicialdata.judicial_user_profile ADD COLUMN date_of_deletion TIMESTAMP;

--Alter dbjudicialdata.judicial_office_appointment
ALTER TABLE dbjudicialdata.judicial_office_appointment ALTER COLUMN base_location_id SET NOT NULL;
ALTER TABLE dbjudicialdata.judicial_office_appointment ALTER COLUMN appointment SET NOT NULL;
ALTER TABLE dbjudicialdata.judicial_office_appointment ALTER COLUMN contract_type_id SET NOT NULL;

--Alter dbjudicialdata.judicial_office_authorisation
ALTER TABLE dbjudicialdata.judicial_office_authorisation ALTER COLUMN jurisdiction SET NOT NULL;
ALTER TABLE dbjudicialdata.judicial_office_authorisation ALTER COLUMN lower_level SET NOT NULL;
ALTER TABLE dbjudicialdata.judicial_office_authorisation ALTER COLUMN ticket_code SET NOT NULL;

--Alter dbjudicialdata.judicial_role_type
ALTER TABLE dbjudicialdata.judicial_additional_roles ALTER COLUMN title SET NOT NULL;


--Alter dbjudicialdata.judicial_office_appointment
ALTER TABLE dbjudicialdata.judicial_office_appointment  DROP CONSTRAINT cft_region_id_fk1;
ALTER TABLE dbjudicialdata.judicial_office_appointment RENAME COLUMN cft_region_id TO hmcts_region_id;
ALTER TABLE dbjudicialdata.judicial_office_appointment ALTER COLUMN hmcts_region_id TYPE varchar(64);

--drop dbjudicialdata.cft_region_type
drop table dbjudicialdata.cft_region_type ;

--create dbjudicialdata.hmcts_region_type
CREATE TABLE dbjudicialdata.hmcts_region_type (
	hmcts_region_id varchar(64) NOT NULL,
	hmcts_region_desc_en varchar(256) NOT NULL,
	hmcts_region_desc_cy varchar(256),
	CONSTRAINT hmcts_region_id PRIMARY KEY (hmcts_region_id)
);

--Alter dbjudicialdata.judicial_office_appointment
alter table dbjudicialdata.judicial_office_appointment add CONSTRAINT hmcts_region_id_fk1 FOREIGN KEY (hmcts_region_id)
REFERENCES dbjudicialdata.hmcts_region_type(hmcts_region_id);

-- Insert table script : dbjudicialdata.hmcts_region_type

insert into dbjudicialdata.hmcts_region_type(hmcts_region_id,hmcts_region_desc_en,hmcts_region_desc_cy) values ('0', 'default', 'default') on conflict (hmcts_region_id) do nothing;
insert into dbjudicialdata.hmcts_region_type(hmcts_region_id,hmcts_region_desc_en,hmcts_region_desc_cy) values ('1', 'London', '');
insert into dbjudicialdata.hmcts_region_type(hmcts_region_id,hmcts_region_desc_en,hmcts_region_desc_cy) values ('2', 'Midlands', '');
insert into dbjudicialdata.hmcts_region_type(hmcts_region_id,hmcts_region_desc_en,hmcts_region_desc_cy) values ('3', 'North East', '');
insert into dbjudicialdata.hmcts_region_type(hmcts_region_id,hmcts_region_desc_en,hmcts_region_desc_cy) values ('4', 'North West', '');
insert into dbjudicialdata.hmcts_region_type(hmcts_region_id,hmcts_region_desc_en,hmcts_region_desc_cy) values ('5', 'South East', '');
insert into dbjudicialdata.hmcts_region_type(hmcts_region_id,hmcts_region_desc_en,hmcts_region_desc_cy) values ('6', 'South West', '');
insert into dbjudicialdata.hmcts_region_type(hmcts_region_id,hmcts_region_desc_en,hmcts_region_desc_cy) values ('7', 'Wales', '');
insert into dbjudicialdata.hmcts_region_type(hmcts_region_id,hmcts_region_desc_en,hmcts_region_desc_cy) values ('10', 'Northern Ireland', '');
insert into dbjudicialdata.hmcts_region_type(hmcts_region_id,hmcts_region_desc_en,hmcts_region_desc_cy) values ('11', 'Scotland', '');
insert into dbjudicialdata.hmcts_region_type(hmcts_region_id,hmcts_region_desc_en,hmcts_region_desc_cy) values ('12', 'National', '');

