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

DELETE FROM judicial_service_code_mapping WHERE service_id in ('21','55');
DELETE FROM judicial_ticket_code_mapping WHERE ticket_code in ('363','376');

DELETE FROM jrd_lrd_region_mapping WHERE jrd_region_id in ('29','30','31','34');

DELETE FROM judicial_location_mapping WHERE epimms_id ='736719' AND judicial_base_location_id ='916';
DELETE FROM judicial_location_mapping WHERE epimms_id ='232580' AND judicial_base_location_id ='981';
DELETE FROM judicial_location_mapping WHERE epimms_id ='415903' AND judicial_base_location_id ='1191';
DELETE FROM judicial_location_mapping WHERE epimms_id ='450049' AND judicial_base_location_id ='1447';
DELETE FROM judicial_location_mapping WHERE epimms_id ='450049' AND judicial_base_location_id ='1448';
DELETE FROM judicial_location_mapping WHERE epimms_id ='640119' AND judicial_base_location_id ='1449';
DELETE FROM judicial_location_mapping WHERE epimms_id ='45900' AND judicial_base_location_id ='1450';
DELETE FROM judicial_location_mapping WHERE epimms_id ='490237' AND judicial_base_location_id ='1451';
DELETE FROM judicial_location_mapping WHERE epimms_id ='366796' AND judicial_base_location_id ='1452';
COMMIT;