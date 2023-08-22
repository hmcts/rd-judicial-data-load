INSERT INTO dbjudicialdata.judicial_user_profile (personal_code,known_as,surname,full_name,post_nominals,ejudiciary_email,active_flag,created_date,last_loaded_date,object_id,sidam_id,initials)
VALUES ('32','One2Kb','One2Sb','One2Fb','Mr','One531@test.net','TRUE','2021-07-14 12:25:28.763','2021-07-14 12:25:28.763','5f8b26ba-0c8b-4192-b5c7-311d737f0qwe','1.11112E+12','J.K');
INSERT INTO dbjudicialdata.judicial_user_profile (personal_code,known_as,surname,full_name,post_nominals,ejudiciary_email,active_flag,created_date,last_loaded_date,object_id,sidam_id,initials)
VALUES ('33','Two2Kb','Two2Sb','Two2Fb','Mr','Two532@test.net','TRUE','2021-07-14 12:25:28.763','2021-07-14 12:25:28.763','5f8b26ba-0c8b-4192-b5c7-311d737f0qrr','1.11112E+12','O.K');
INSERT INTO dbjudicialdata.judicial_user_profile (personal_code,known_as,surname,full_name,post_nominals,ejudiciary_email,active_flag,created_date,last_loaded_date,object_id,sidam_id,initials)
VALUES ('34','Three2Kb','Three2Sb','Three2Fb','Mr','Three532@test.net','TRUE','2021-07-14 12:25:28.763','2021-07-14 12:25:28.763','5f8b26ba-0c8b-4192-b5c7-311d737f0ret','1.11112E+12','T.K'),
('35','Four2Kb','Four2Sb','Four2Fb','Mr','Four534@test.net','TRUE','2021-07-14 12:25:28.763','2021-07-14 12:25:28.763','5f8b26ba-0c8b-4192-b5c7-311d737f0ytt','1.11112E+12','F.K'),
('36','Five2Kb','Five2Sb','Five2Fb','Mr','Five535@test.net','TRUE','2021-07-14 12:25:28.763','2021-07-14 12:25:28.763','5f8b26ba-0c8b-4192-b5c7-rttd737f0ytt','1.11112E+12','F.K'),
('37','SIX2Kb','Six2Sb','Six2Fb','Mr','Six536@test.net','TRUE','2021-07-14 12:25:28.763','2021-07-14 12:25:28.763','5f8b26ba-0rtb-4192-b5c7-rttd737f0ytt','1.11112E+12','F.K'),
('38','Seven2Kb','Seven2Sb','Seven2Fb','Mr','Seven537@test.net','TRUE','2021-07-14 12:25:28.763','2021-07-14 12:25:28.763','5f8msdba-0rtb-4192-b5c7-rttd737f0ytt','1.11112E+12','S.K');

INSERT INTO dbjudicialdata.location_type (base_location_id,"name",type_id,parent_id,jurisdiction_id,start_date,end_date,created_at,updated_at) values  ('1032','Social Entitlement Chamber','42','1503','27',NULL,NULL,'2023-05-02 11:43:53','2023-05-02 11:43:53');


INSERT INTO dbjudicialdata.location_type (base_location_id,"name",type_id,parent_id,jurisdiction_id,start_date,end_date,created_at,updated_at) values ('1036','Employment Tribunal (England & Wales)','42','1674','27',NULL,NULL,'2023-05-02 11:43:55','2023-05-02 11:43:55');
INSERT INTO dbjudicialdata.location_type (base_location_id,"name",type_id,parent_id,jurisdiction_id,start_date,end_date,created_at,updated_at) values ('1030','Immigration and Asylum Chamber','42','1503','27',NULL,NULL,'2023-05-02 11:43:52','2023-05-02 11:43:52');
INSERT INTO dbjudicialdata.location_type (base_location_id,"name",type_id,parent_id,jurisdiction_id,start_date,end_date,created_at,updated_at) values ('1037','Employment Tribunal Scotland','42','1674','27',NULL,NULL,'2023-05-02 11:43:55','2023-05-02 11:43:55');


INSERT INTO dbjudicialdata.judicial_office_appointment (personal_code,base_location_id,hmcts_region_id,is_prinicple_appointment,start_date,end_date,created_date,last_loaded_date,epimms_id,appointment,appointment_type,appointment_id,role_name_id,"type",contract_type_id,"location",jo_base_location_id) VALUES
 ('32','1032','1',true,'1995-03-27', NULL,'2023-08-03 13:28:10.188406','2023-08-03 13:28:10.188432',NULL,'Magistrate','Voluntary','195','1','LJA','2','South East','1032'),
 ('32','1030','1',true,'1995-03-27', NULL,'2023-08-03 13:28:10.188406','2023-08-03 13:28:10.188432',NULL,'Magistrate','Voluntary','196','1','LJA','2','South East','1030'),
 ('33','1032','1',true,'1995-03-27', '2023-03-27','2023-08-03 13:28:10.188406','2023-08-03 13:28:10.188432',NULL,'Magistrate','Voluntary','197','1','LJA','2','South East','1032'),
 ('33','1030','1',true,'1995-03-27', '2023-03-27','2023-08-03 13:28:10.188406','2023-08-03 13:28:10.188432',NULL,'Magistrate','Voluntary','198','1','LJA','2','South East','1030'),
 ('34','1032','1',true,'1995-03-27', NULL,'2023-08-03 13:28:10.188406','2023-08-03 13:28:10.188432',NULL,'Magistrate','Voluntary','199','1','LJA','2','South East','1032'),
 ('34','1030','1',true,'1995-03-27', NULL,'2023-08-03 13:28:10.188406','2023-08-03 13:28:10.188432',NULL,'Magistrate','Voluntary','200','1','LJA','2','South East','1030'),
 ('35','1032','1',true,'1995-03-27', '2022-03-27','2023-08-03 13:28:10.188406','2023-08-03 13:28:10.188432',NULL,'Magistrate','Voluntary','201','1','LJA','2','South East','1032'),
 ('35','1030','1',true,'1995-03-27', '2022-03-27','2023-08-03 13:28:10.188406','2023-08-03 13:28:10.188432',NULL,'Magistrate','Voluntary','202','1','LJA','2','South East','1030'),
 ('36','1032','1',true,'1995-03-27', NULL,'2023-08-03 13:28:10.188406','2023-08-03 13:28:10.188432',NULL,'Magistrate','Voluntary','203','1','LJA','2','South East','1032'),
 ('36','1030','1',true,'1995-03-27', NULL,'2023-08-03 13:28:10.188406','2023-08-03 13:28:10.188432',NULL,'Magistrate','Voluntary','204','1','LJA','2','South East','1030'),
 ('37','1032','1',true,'1995-03-27', '2022-03-27','2023-08-03 13:28:10.188406','2023-08-03 13:28:10.188432',NULL,'Magistrate','Voluntary','205','1','LJA','2','South East','1032'),
 ('37','1030','1',true,'1995-03-27', NULL,'2023-08-03 13:28:10.188406','2023-08-03 13:28:10.188432',NULL,'Magistrate','Voluntary','206','1','LJA','2','South East','1030'),
 ('38','1036','1',true,'1995-03-27', NULL,'2023-08-03 13:28:10.188406','2023-08-03 13:28:10.188432',NULL,'Magistrate','Voluntary','207','1','LJA','2','South East','1036'),
 ('38','1037','1',true,'1995-03-27', NULL,'2023-08-03 13:28:10.188406','2023-08-03 13:28:10.188432',NULL,'Magistrate','Voluntary','208','1','LJA','2','South East','1037');


 INSERT INTO dbjudicialdata.judicial_office_authorisation
 (judicial_office_auth_id, jurisdiction, start_date, end_date, created_date,
 last_updated, lower_level, personal_code, ticket_code,authorisation_id,jurisdiction_id,appointment_id)
 VALUES(1023, 'Authorisation Magistrate', '2002-09-09 00:00:00.000', NULL,
 '2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '32', '368',7,1,'195'),
 (1024, 'Authorisation Magistrate', '2002-09-09 00:00:00.000', NULL,
 '2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '32', '373',8,2,'196'),
 (1025, 'Authorisation Magistrate', '2002-09-09 00:00:00.000', '2021-09-09 00:00:00.000',
 '2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '33', '368',7,1,'197'),
 (1026, 'Authorisation Magistrate', '2002-09-09 00:00:00.000', '2022-09-09 00:00:00.000',
 '2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '33', '373',8,2,'198'),
 (1027, 'Authorisation Magistrate', '2002-09-09 00:00:00.000', '2022-09-09 00:00:00.000',
 '2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '34', '368',8,2,'199'),
 (1028, 'Authorisation Magistrate', '2002-09-09 00:00:00.000', '2022-09-09 00:00:00.000',
 '2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '34', '373',8,2,'200'),
 (1029, 'Authorisation Magistrate', '2002-09-09 00:00:00.000',  NULL,
 '2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '35', '368',8,2,'201'),
 (1030, 'Authorisation Magistrate', '2002-09-09 00:00:00.000',  NULL,
 '2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '35', '373',8,2,'202'),
 (1031, 'Authorisation Magistrate', '2002-09-09 00:00:00.000',  NULL,
 '2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '36', '368',8,2,'203'),
 (1032, 'Authorisation Magistrate', '2002-09-09 00:00:00.000',  '2021-09-09 00:00:00.000',
 '2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '36', '373',8,2,'204'),
 (1033, 'Authorisation Magistrate', '2002-09-09 00:00:00.000',  NULL,
 '2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '37', '368',8,2,'205'),
 (1034, 'Authorisation Magistrate', '2002-09-09 00:00:00.000',  NULL,
 '2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '37', '373',8,2,'206'),
 (1035, 'Authorisation Magistrate', '2002-09-09 00:00:00.000',  '2021-09-09 00:00:00.000',
 '2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '38', '279',8,2,'207'),
 (1036, 'Authorisation Magistrate', '2002-09-09 00:00:00.000',  NULL,
 '2021-08-11 09:14:30.054', '2021-08-11 09:14:30.054', 'Family Court', '38', '379',8,2,'208');