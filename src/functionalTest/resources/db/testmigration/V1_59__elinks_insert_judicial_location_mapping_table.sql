INSERT INTO dbjudicialdata.region_type
(region_id, region_desc_en, region_desc_cy)
VALUES('1', 'National', NULL);

INSERT INTO dbjudicialdata.base_location_type
(base_location_id, court_name, court_type, circuit, area_of_expertise)
VALUES('1029', 'Aberconwy', 'Old Gwynedd', 'National', 'LJA');


INSERT INTO dbjudicialdata.judicial_user_profile (personal_code,known_as,surname,full_name,post_nominals,ejudiciary_email,active_flag,created_date,last_loaded_date,object_id,sidam_id,initials)
VALUES ('27','Test2KA','Test2SN','Test2FN','Mr','test528@test.net','TRUE','2021-07-14 12:25:28.763','2021-07-14 12:25:28.763','1.11112E+12','1.11112E+12','S.K');

INSERT INTO dbjudicialdata.judicial_user_profile (personal_code,known_as,surname,full_name,post_nominals,ejudiciary_email,active_flag,created_date,last_loaded_date,object_id,sidam_id,initials)
VALUES ('28','TestKA','TestSN','TestFN','Ms','test529@test.net','TRUE','2021-07-14 12:25:28.763','2021-08-11 09:10:44.682','1.11112E+12','1.11112E+12','M.J');

INSERT INTO dbjudicialdata.judicial_user_profile (personal_code,known_as,surname,full_name,post_nominals,ejudiciary_email,active_flag,created_date,last_loaded_date,object_id,sidam_id,initials)
VALUES ('29','Test1KA','Test1SN','Test1FN','Ms','test530@test.net','TRUE','2021-07-14 12:25:28.763','2021-08-11 09:10:44.682','1.11112E+12','1.11112E+12','B.K');

INSERT INTO dbjudicialdata.judicial_office_appointment
(judicial_office_appointment_id, base_location_id, region_id, is_prinicple_appointment,
start_date, end_date, created_date, last_loaded_date, personal_code,
epimms_id, service_code, object_id, appointment, appointment_type,appointment_id)
VALUES(179, '1029', '1', true, '1995-03-27', NULL,
'2021-08-11 09:12:40.134', '2021-08-11 09:12:40.134', '27', NULL, 'BFA1', '1111122223333', 'TestApp', 'TestApptype',179),
(180, '1029', '1', true, '1995-03-27', NULL,
'2021-08-11 09:12:40.134', '2021-08-11 09:12:40.134', '27', NULL, 'BBA3', '1111122223333', 'TestApp', 'TestApptype',180),
(181, '1029', '1', true, '1995-03-27', NULL,
'2021-08-11 09:12:40.134', '2021-08-11 09:12:40.134', '28', NULL, NULL, '1111122223333', 'TestApp', 'TestApptype',181),
(182, '1029', '1', true, '1995-03-27', NULL,
'2021-08-11 09:12:40.134', '2021-08-11 09:12:40.134', '29', '20012', 'BFA1', '1111122223333', 'TestApp', 'TestApptype',182),
(183, '1029', '1', true, '1995-03-27', NULL,
'2021-08-11 09:12:40.134', '2021-08-11 09:12:40.134', '27', '20013', 'BBA3', '1111122223333', 'TestApp', 'TestApptype',183),
(184, '1029', '1', true, '1995-03-27', NULL,
'2021-08-11 09:12:40.134', '2021-08-11 09:12:40.134', '27', '20014', 'AAA6', '1111122223333', 'TestApp', 'TestApptype',184),
(185, '1029', '1', true, '1995-03-27', NULL,
'2021-08-11 09:12:40.134', '2021-08-11 09:12:40.134', '27', '20014', 'AAA7', '1111122223333', 'TestApp', 'TestApptype',185),
(186, '1029', '1', true, '1995-03-27', NULL,
'2021-08-11 09:12:40.134', '2021-08-11 09:12:40.134', '27', '20014', 'ABA3', '1111122223333', 'TestApp', 'TestApptype',186),
(187, '1029', '1', true, '1995-03-27', NULL,
'2021-08-11 09:12:40.134', '2021-08-11 09:12:40.134', '27', '20014', 'ABA5', '1111122223333', 'TestApp', 'TestApptype',187);


INSERT INTO dbjudicialdata.judicial_office_authorisation
(judicial_office_auth_id, jurisdiction, start_date, end_date, created_date,
last_updated, lower_level, personal_code, ticket_code, object_id,authorisation_id,jurisdiction_id)
VALUES(1015, 'Authorisation Magistrate', '2002-09-09 00:00:00.000', NULL,
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '27', NULL, '1111122223333',7,1),
(1016, 'Authorisation Magistrate', '2002-09-09 00:00:00.000', NULL,
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '27', '373', '1111122223333',8,2),
(1017, 'Authorisation Magistrate', '2002-09-09 00:00:00.000', NULL,
'2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '28', '364', '1111122223333',9,3);

INSERT INTO dbjudicialdata.judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code) VALUES
	 ('1123', '1815', '', 'Employment Tribunal England and Wales', 'BHA1'),
	 ('1126', '768', '', 'Employment Tribunal Scotland', 'BFA1');
COMMIT; 