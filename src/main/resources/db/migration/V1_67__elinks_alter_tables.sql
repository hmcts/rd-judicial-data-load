--Alter dbjudicialdata.judicial_office_appointment
ALTER TABLE dbjudicialdata.judicial_office_appointment DROP COLUMN service_code;
ALTER TABLE dbjudicialdata.judicial_office_appointment DROP COLUMN object_id;

--Alter dbjudicialdata.judicial_office_authorisation
ALTER TABLE dbjudicialdata.judicial_office_authorisation DROP COLUMN object_id;

--Alter dbjudicialdata.judicial_role_type
ALTER TABLE dbjudicialdata.judicial_role_type RENAME TO judicial_additional_roles;

--Alter dbjudicialdata.judicial_location_mapping
ALTER TABLE dbjudicialdata.judicial_location_mapping DROP COLUMN building_location_name;
ALTER TABLE dbjudicialdata.judicial_location_mapping DROP COLUMN base_location_name;

--Alter dbjudicialdata.judicial_ticket_code_mapping
ALTER TABLE dbjudicialdata.judicial_ticket_code_mapping RENAME TO judicial_ticket_code_type;
ALTER TABLE dbjudicialdata.judicial_ticket_code_type RENAME COLUMN jurisdiction TO ticket_category_id;

--delete data from dbjudicialdata.judicial_ticket_code_type
delete from dbjudicialdata.judicial_ticket_code_type;

--Alter dbjudicialdata.judicial_ticket_code_type
ALTER TABLE dbjudicialdata.judicial_ticket_code_type ALTER COLUMN ticket_category_id TYPE varchar(16);

