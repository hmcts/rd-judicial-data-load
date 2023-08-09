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
