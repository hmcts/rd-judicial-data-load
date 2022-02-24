INSERT INTO judicial_location_mapping (epimms_id, judicial_base_location_id, building_location_name, base_location_name, service_code) VALUES('', '1032', '', 'Social Entitlement', 'BBA3');
DELETE FROM judicial_service_code_mapping WHERE service_id in ('21','55');
DELETE FROM judicial_ticket_code_mapping WHERE ticket_code in ('363','376');
COMMIT;
