--Alter judicial_user_profile
ALTER TABLE judicial_user_profile DROP COLUMN is_judge;
ALTER TABLE judicial_user_profile DROP COLUMN is_panel_member;
ALTER TABLE judicial_user_profile DROP COLUMN is_magistrate;
ALTER TABLE judicial_user_profile DROP COLUMN mrd_created_time;
ALTER TABLE judicial_user_profile DROP COLUMN mrd_updated_time;
ALTER TABLE judicial_user_profile DROP COLUMN mrd_deleted_time;
--Alter judicial_office_appointment
ALTER TABLE judicial_office_appointment DROP COLUMN primary_location;
ALTER TABLE judicial_office_appointment DROP COLUMN secondary_location;
ALTER TABLE judicial_office_appointment DROP COLUMN tertiary_location;
ALTER TABLE judicial_office_appointment DROP COLUMN mrd_created_time;
ALTER TABLE judicial_office_appointment DROP COLUMN mrd_updated_time;
ALTER TABLE judicial_office_appointment DROP COLUMN mrd_deleted_time;
--Alter judicial_office_authorisation
ALTER TABLE judicial_office_authorisation DROP COLUMN mrd_created_time;
ALTER TABLE judicial_office_authorisation DROP COLUMN mrd_updated_time;
ALTER TABLE judicial_office_authorisation DROP COLUMN mrd_deleted_time;
--Alter judicial_role_type
ALTER TABLE  judicial_role_type DROP COLUMN mrd_created_time;
ALTER TABLE  judicial_role_type DROP COLUMN mrd_updated_time;
ALTER TABLE  judicial_role_type DROP COLUMN mrd_deleted_time;
--Alter judicial_location_mapping
ALTER TABLE judicial_location_mapping DROP COLUMN mrd_created_time;
ALTER TABLE judicial_location_mapping DROP COLUMN mrd_updated_time;
ALTER TABLE judicial_location_mapping DROP COLUMN mrd_deleted_time;
--Alter judicial_service_code_mapping
ALTER TABLE judicial_service_code_mapping DROP COLUMN mrd_created_time;
ALTER TABLE judicial_service_code_mapping DROP COLUMN mrd_updated_time;
ALTER TABLE judicial_service_code_mapping DROP COLUMN mrd_deleted_time;
--Alter judicial_ticket_code_mapping
ALTER TABLE judicial_ticket_code_mapping DROP COLUMN mrd_created_time;
ALTER TABLE judicial_ticket_code_mapping DROP COLUMN mrd_updated_time;
ALTER TABLE judicial_ticket_code_mapping DROP COLUMN mrd_deleted_time;
--Alter base_location_type
ALTER TABLE base_location_type DROP COLUMN mrd_created_time;
ALTER TABLE base_location_type DROP COLUMN mrd_updated_time;
ALTER TABLE base_location_type DROP COLUMN mrd_deleted_time;
--Alter region_type
ALTER TABLE region_type DROP COLUMN mrd_created_time;
ALTER TABLE region_type DROP COLUMN mrd_updated_time;
ALTER TABLE region_type DROP COLUMN mrd_deleted_time;
--Alter jrd_lrd_region_mapping
ALTER TABLE jrd_lrd_region_mapping DROP COLUMN mrd_created_time;
ALTER TABLE jrd_lrd_region_mapping DROP COLUMN mrd_updated_time;
ALTER TABLE jrd_lrd_region_mapping DROP COLUMN mrd_deleted_time;