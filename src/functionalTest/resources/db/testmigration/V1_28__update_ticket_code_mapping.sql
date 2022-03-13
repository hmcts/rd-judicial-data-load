UPDATE judicial_ticket_code_mapping SET lower_level = 'Section 9(1) Chancery' WHERE ticket_code = '300';
UPDATE judicial_ticket_code_mapping SET lower_level = 'Section 9(1) Queens Bench' WHERE ticket_code = '301';
UPDATE judicial_ticket_code_mapping SET lower_level = 'Section 9(1) Queens Bench - Admin ONLY' WHERE ticket_code = '393';
UPDATE judicial_ticket_code_mapping SET lower_level = 'Section 9(1) IPEC' WHERE ticket_code = '396';
UPDATE judicial_ticket_code_mapping SET lower_level = 'S9(4) Appointment - Chancery' WHERE ticket_code = '397';
UPDATE judicial_ticket_code_mapping SET lower_level = 'S9(4) Appointment - Queens Bench' WHERE ticket_code = '398';
UPDATE judicial_ticket_code_mapping SET lower_level = 'S9(4) Appointment - Family' WHERE ticket_code = '400';

--Alter region_type
ALTER TABLE region_type ADD COLUMN mrd_created_time TIMESTAMP;
ALTER TABLE region_type ADD COLUMN mrd_updated_time TIMESTAMP;
ALTER TABLE region_type ADD COLUMN mrd_deleted_time TIMESTAMP;

COMMIT;