ALTER TABLE judicial_office_appointment ADD COLUMN service_code varchar(16);
ALTER TABLE judicial_office_appointment ADD COLUMN object_id varchar(64);

ALTER TABLE judicial_office_authorisation ADD COLUMN object_id varchar(64);

ALTER TABLE judicial_location_mapping ADD COLUMN service_code varchar(16);

INSERT INTO judicial_location_mapping (judicial_base_location_id,base_location_name,epimms_id,building_location_name, service_code)
VALUES ('1030','Immigration and Asylum First Tier','','','BFA1');