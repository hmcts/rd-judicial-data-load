--Alter judicial_user_profile
ALTER TABLE judicial_user_profile ADD COLUMN is_judge BOOLEAN;
ALTER TABLE judicial_user_profile ADD COLUMN is_panel_member BOOLEAN;
ALTER TABLE judicial_user_profile ADD COLUMN is_magistrate BOOLEAN;
ALTER TABLE judicial_user_profile ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE judicial_user_profile ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE judicial_user_profile ADD COLUMN mrd_deleted_time TIMESTAMP;
--Alter judicial_office_authorisation
ALTER TABLE judicial_office_authorisation ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE judicial_office_authorisation ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE judicial_office_authorisation ADD COLUMN mrd_deleted_time TIMESTAMP;
--Alter judicial_role_type
ALTER TABLE  judicial_role_type ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE  judicial_role_type ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE  judicial_role_type ADD COLUMN mrd_deleted_time TIMESTAMP;
--Alter judicial_location_mapping
ALTER TABLE judicial_location_mapping ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE judicial_location_mapping ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE judicial_location_mapping ADD COLUMN mrd_deleted_time TIMESTAMP;
--Alter judicial_service_code_mapping
ALTER TABLE judicial_service_code_mapping ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE judicial_service_code_mapping ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE judicial_service_code_mapping ADD COLUMN mrd_deleted_time TIMESTAMP;
--Alter judicial_ticket_code_mapping
ALTER TABLE judicial_ticket_code_mapping ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE judicial_ticket_code_mapping ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE judicial_ticket_code_mapping ADD COLUMN mrd_deleted_time TIMESTAMP;
--Alter base_location_type
ALTER TABLE base_location_type ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE base_location_type ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE base_location_type ADD COLUMN mrd_deleted_time TIMESTAMP;
--Alter region_type
ALTER TABLE region_type ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE region_type ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE region_type ADD COLUMN mrd_deleted_time TIMESTAMP;
--Alter jrd_lrd_region_mapping
ALTER TABLE jrd_lrd_region_mapping ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE jrd_lrd_region_mapping ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE jrd_lrd_region_mapping ADD COLUMN mrd_deleted_time TIMESTAMP;
--Alter judicial_office_appointment
ALTER TABLE judicial_office_appointment ADD COLUMN primary_location VARCHAR(16);
ALTER TABLE judicial_office_appointment ADD COLUMN secondary_location VARCHAR(16);
ALTER TABLE judicial_office_appointment ADD COLUMN tertiary_location VARCHAR(16);
ALTER TABLE judicial_office_appointment ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE judicial_office_appointment ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE judicial_office_appointment ADD COLUMN mrd_deleted_time TIMESTAMP;
COMMIT;