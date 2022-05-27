truncate table  judicial_location_mapping  cascade;
truncate table  jrd_lrd_region_mapping cascade;
truncate table  judicial_service_code_mapping cascade;
truncate table  judicial_ticket_code_mapping cascade;

INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('20262','747','ROYAL COURTS OF JUSTICE','Royal Courts of Justice - High Court - Chancery','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('20262','748','ROYAL COURTS OF JUSTICE','Royal Courts of Justice - High Court - Family','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('20262','749','ROYAL COURTS OF JUSTICE','Royal Courts of Justice - High Court - QB','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('20262','750','ROYAL COURTS OF JUSTICE','Royal Courts of Justice - Court of Appeal','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('312962','752','CENTRAL CRIMINAL COURT','Central Criminal Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('407494','753','CROYDON COUNTY COURT AND FAMILY COURT','Croydon Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('29096','754','HARROW CROWN COURT','Harrow Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('337856','755','INNER LONDON CROWN COURT','Inner London Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('718075','756','ISLEWORTH CROWN COURT','Isleworth Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('699560','757','KINGSTON UPON THAMES CROWN COURT','Kingston Upon Thames Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('127994','758','SNARESBROOK CROWN COURT','Snaresbrook Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('337959','759','SOUTHWARK CROWN COURT','Southwark Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('403689','760','WOOD GREEN CROWN COURT','Wood Green Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('353988','761','WOOLWICH CROWN COURT','Woolwich Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('229786','762','BARNET CIVIL AND FAMILY COURTS CENTRE','Barnet County Court ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('36791','764','BRENTFORD COUNTY COURT AND FAMILY COURT','Brentford County Court ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('29656','765','BROMLEY COUNTY COURT AND FAMILY COURT','Bromley County Court ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('356855','766','CENTRAL FAMILY COURT (First Avenue House)','Central Family Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('20262','767','ROYAL COURTS OF JUSTICE','Central London County Court ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('739514','769','CLERKENWELL AND SHOREDITCH COUNTY COURT AND FAMILY COURT','Clerkenwell and Shoreditch County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('407494','770','CROYDON COUNTY COURT AND FAMILY COURT','Croydon County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('898213','771','EAST LONDON FAMILY COURT','East London Family Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('25463','772','EDMONTON COUNTY COURT AND FAMILY COURT','Edmonton County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('13660','773','KINGSTON UPON THAMES COUNTY COURT AND FAMILY COURT','Kingston Upon Thames County Court ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('674229','775','Mayor''s and City of London Court','Major& City County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('400947','776','ROMFORD COUNTY COURT AND FAMILY COURT','Romford County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('621184','777','UXBRIDGE COUNTY COURT AND FAMILY COURT','Uxbridge County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('268374','778','WANDSWORTH COUNTY COURT AND FAMILY COURT','Wandsworth County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('373584','780','WEST LONDON FAMILY COURT','West London Family Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('228015','781','WILLESDEN COUNTY COURT AND FAMILY COURT','Willesden County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('817181','783','AYLESBURY CROWN COURT','Aylesbury Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('694840','784','BASILDON COMBINED COURT','Basildon Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('507931','785','CAMBRIDGE CROWN COURT','Cambridge Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('259679','786','CANTERBURY COMBINED COURT CENTRE','Canterbury Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('428073','787','CHELMSFORD CROWN COURT','Chelmsford Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('468679','789','GUILDFORD CROWN COURT','Guildford Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('357989','790','IPSWICH CROWN COURT','Ipswich Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('101183','791','LEWES COMBINED COURT CENTRE','Lewes Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('486853','792','LUTON CROWN COURT','Luton Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('465872','793','MAIDSTONE COMBINED COURT CENTRE','Maidstone Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('471332','794','NORWICH COMBINED COURT CENTRE','Norwich Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('371016','795','OXFORD COMBINED COURT CENTRE','Oxford Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('471569','796','PETERBOROUGH COMBINED COURT CENTRE','Peterborough Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('256379','797','READING CROWN COURT','Reading Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('198303','798','ST ALBANS CROWN COURT','St Albans Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('694840','802','BASILDON COMBINED COURT','Basildon County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('446255','803','BEDFORD COUNTY COURT AND FAMILY COURT','Bedford County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('478896','804','BRIGHTON COUNTY AND FAMILY COURT','Brighton County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('298390','805','BRIGHTON HEARING CENTRE','Brighton Family Centre','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('257431','806','BURY ST EDMUNDS COUNTY COURT AND FAMILY COURT','Bury St Edmunds County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('650344','807','CAMBRIDGE COUNTY COURT AND FAMILY COURT','Cambridge County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('259679','808','CANTERBURY COMBINED COURT CENTRE','Canterbury County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('816875','809','CHELMSFORD COUNTY AND FAMILY COURT','Chelmsford County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('194172','812','DARTFORD COUNTY COURT AND FAMILY COURT','Dartford County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('609320','814','GREAT YARMOUTH MAGISTRATES COURT AND FAMILY COURT','Great Yarmouth Family Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('568484','815','GUILDFORD MAGISTRATES COURT AND FAMILY COURT','Guildford County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('784691','816','HASTINGS MAGISTRATES COURT','Hastings County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('256913','817','HERTFORD COUNTY COURT AND FAMILY COURT','Hertford County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('437303','818','HIGH WYCOMBE MAGISTRATES COURT AND FAMILY COURT','High Wycombe County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('317442','819','HORSHAM COUNTY COURT AND FAMILY COURT','Horsham County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('471349','820','IPSWICH COUNTY COURT AND FAMILY HEARING CENTRE','Ipswich County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('101183','822','LEWES COMBINED COURT CENTRE','Lewes County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('259134','825','Luton Justice Centre ','Luton County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('465872','826','MAIDSTONE COMBINED COURT CENTRE','Maidstone County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('487294','827','MEDWAY COUNTY COURT AND FAMILY COURT','Medway County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('815997','828','MILTON KEYNES COUNTY COURT AND FAMILY COURT','Milton Keynes County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('471332','829','NORWICH COMBINED COURT CENTRE','Norwich County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('371016','830','OXFORD COMBINED COURT CENTRE','Oxford County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('471569','831','PETERBOROUGH COMBINED COURT CENTRE','Peterborough County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('185657','832','READING COUNTY COURT AND FAMILY COURT','Reading County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('224403','834','SLOUGH COUNTY COURT AND FAMILY COURT','Slough County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('781139','835','SOUTHEND COMBINED - CROWN, MAGS, COUNTY AND FAMILY COURTS','Southend County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('298828','836','STAINES MAGISTRATES COURT AND FAMILY COURT','Staines County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('659591','838','MARGATE MAGISTRATES COURT','Thanet County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('403751','840','WATFORD COUNTY COURT AND FAMILY COURT','Watford County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('493880','841','WORTHING MAGISTRATES AND COUNTY COURT','Worthing County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('411234','842','BATH MAGISTRATES, COUNTY AND FAMILY COURT','Bath County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('819890','843','BRISTOL CIVIL AND FAMILY JUSTICE CENTRE','Bristol County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('477819','844','BRISTOL CROWN COURT','Bristol Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('198592','845','GLOUCESTER AND CHELTENHAM COUNTY  AND FAMILY COURT','Gloucester and Cheltenham County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('100539','846','GLOUCESTER CROWN COURT','Gloucester Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('382409','847','TAUNTON CROWN, COUNTY AND FAMILY COURT','Taunton County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('382409','848','TAUNTON CROWN, COUNTY AND FAMILY COURT','Taunton Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('545334','849','NORTH SOMERSET MAGISTRATES','Weston-super-Mare County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('315404','850','YEOVIL COUNTY, FAMILY AND MAGISTRATES COURT','Yeovil County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('774335','851','BARNSTAPLE MAGISTRATES, COUNTY AND FAMILY COURT','Barnstaple County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('271813','853','BODMIN COUNTY COURT AND FAMILY COURT','Bodmin County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('101633','854','BOURNEMOUTH COMBINED COURT','Bournemouth Combined Court- Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('101633','855','BOURNEMOUTH COMBINED COURT','Bournemouth Combined Court- County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('624161','856','WEYMOUTH COMBINED COURT','Weymouth and Dorchester Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('624161','858','WEYMOUTH COMBINED COURT','Weymouth and Dorchester Combined Court - Family','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('735217','859','EXETER COMBINED COURT','Exeter Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('735217','860','EXETER COMBINED COURT','Exeter Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('339463','861','PLYMOUTH COMBINED COURT','Plymouth Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('339463','862','PLYMOUTH COMBINED COURT','Plymouth Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('235617','863','TORQUAY AND NEWTON ABBOT COUNTY COURT AND FAMILY COURT','Torquay and Newton Abbot County Court ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('475776','864','TRURO COMBINED COURT','Truro Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('475776','865','TRURO COMBINED COURT','Truro Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('457273','867','BASINGSTOKE COUNTY COURT AND FAMILY COURT','Basingstoke Family Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('457273','868','BASINGSTOKE COUNTY COURT AND FAMILY COURT','Basingstoke County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('416742','871','ISLE OF WIGHT COMBINED COURT','Newport IOW Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('416742','872','ISLE OF WIGHT COMBINED COURT','Newport IOW Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('460592','873','PORTSMOUTH COMBINED COURT CENTRE','Portsmouth Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('460592','874','PORTSMOUTH COMBINED COURT CENTRE','Portsmouth Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('817113','875','SALISBURY LAW COURTS','Salisbury Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('817113','876','SALISBURY LAW COURTS','Salisbury Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('43104','877','SOUTHAMPTON COMBINED COURT CENTRE','Southampton Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('43104','878','SOUTHAMPTON COMBINED COURT CENTRE','Southampton Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('438850','879','SWINDON COMBINED COURT','Swindon Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('438850','880','SWINDON COMBINED COURT','Swindon Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('886493','881','WINCHESTER COMBINED COURT CENTRE','Winchester Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('886493','882','WINCHESTER COMBINED COURT CENTRE','Winchester Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('652852','884','CHESTERFIELD MAGISTRATES','Chesterfield County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('201339','885','DERBY COMBINED COURT CENTRE','Derby Combined Court - County ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('201339','886','DERBY COMBINED COURT CENTRE','Derby Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('455368','887','MANSFIELD MAGISTRATES AND COUNTY COURT','Mansfield County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('424213','888','NOTTINGHAM COUNTY COURT AND FAMILY COURT','Nottingham County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('424213','889','NOTTINGHAM COUNTY COURT AND FAMILY COURT','Nottingham Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('117667','890','BOSTON COUNTY COURT AND FAMILY COURT','Boston County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('223503','892','LEICESTER COUNTY COURT','Leicester County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('425094','893','LEICESTER CROWN COURT','Leicester Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('195465','894','LINCOLN COUNTY COURT AND FAMILY COURT','Lincoln County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('318389','895','LINCOLN CROWN COURT','Lincoln Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('195489','896','NORTHAMPTON CROWN COURT, COUNTY COURT AND FAMILY COURT','Northampton Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('195489','897','NORTHAMPTON CROWN COURT, COUNTY COURT AND FAMILY COURT','Northampton Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('228883','898','HEREFORD MAGISTRATES COURT','Hereford County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('259170','899','SHREWSBURY JUSTICE CENTRE','Shrewsbury Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('195472','900','STAFFORD COMBINED COURT CENTRE','Stafford Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('195472','901','STAFFORD COMBINED COURT CENTRE','Stafford Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('195496','902','STOKE-ON-TRENT COMBINED COURT','Stoke on Trent Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('195496','903','STOKE-ON-TRENT COMBINED COURT','Stoke on Trent Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('292771','904','TELFORD MAGISTRATES COURT','Telford County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('102050','905','WORCESTER COMBINED COURT','Worcester Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('102050','906','WORCESTER COMBINED COURT','Worcester Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('482914','907','BIRMINGHAM CROWN COURT','Birmingham Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('231596','908','BIRMINGHAM CIVIL AND FAMILY JUSTICE CENTRE','Birmingham Civil & Family Justice Centre - Family','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('231596','909','BIRMINGHAM CIVIL AND FAMILY JUSTICE CENTRE','Birmingham Civil & Family Justice Centre - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('497679','910','COVENTRY COMBINED COURT CENTRE','Coventry Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('497679','911','COVENTRY COMBINED COURT CENTRE','Coventry Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('758998','912','DUDLEY MAGISTRATES COURT','Dudley County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('329990','913','NUNEATON MAGISTRATES'' COURT','Nuneaton County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('177463','914','WALSALL COUNTY AND FAMILY COURT','Walsall County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('736719','915','LEAMINGTON SPA MAGISTRATES'' COURT','Warwick Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('41047','917','WOLVERHAMPTON COMBINED COURT CENTRE','Wolverhampton Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('41047','918','WOLVERHAMPTON COMBINED COURT CENTRE','Wolverhampton Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('574546','919','BARNSLEY LAW COURTS','Barnsley County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('88516','920','BRADFORD COMBINED COURT CENTRE','Bradford Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('88516','921','BRADFORD COMBINED COURT CENTRE','Bradford Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('288691','922','DARLINGTON COUNTY COURT AND FAMILY COURT','Darlington County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('45900','925','DONCASTER JUSTICE CENTRE SOUTH','Doncaster Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('491107','926','DURHAM JUSTICE CENTRE','Durham County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('386393','928','DURHAM CROWN COURT','Durham Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('427519','929','GATESHEAD MAGISTRATES COURT AND FAMILY COURT','Gateshead County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('478126','930','GREAT GRIMSBY COMBINED COURT CENTRE','Great Grimsby Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('478126','931','GREAT GRIMSBY COMBINED COURT CENTRE','Great Grimsby Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('505683','933','HARROGATE JUSTICE CENTRE','Harrogate County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('197852','935','HUDDERSFIELD COUNTY COURT AND FAMILY COURT','Huddersfield County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('195520','936','KINGSTON-UPON-HULL COMBINED COURT CENTRE','Kingston Upon Hull Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('195520','937','KINGSTON-UPON-HULL COMBINED COURT CENTRE','Kingston Upon Hull Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('455174','938','LEEDS COMBINED COURT CENTRE','Leeds Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('455174','939','LEEDS COMBINED COURT CENTRE','Leeds Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('659436','943','NEWTON AYCLIFFE MAGISTRATES COURT AND FAMILY COURT','Newton Aycliffe Family Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('562808','945','NORTH SHIELDS COUNTY COURT AND FAMILY COURT','North Shields County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('744412','947','SCARBOROUGH JUSTICE CENTRE','Scarborough County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('232607','949','SHEFFIELD COMBINED COURT CENTRE','Sheffield Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('232607','950','SHEFFIELD COMBINED COURT CENTRE','Sheffield Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('318324','951','SKIPTON MAGISTRATES AND COUNTY COURT','Skipton County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('563156','952','SOUTH TYNESIDE MAGISTRATES COURT AND FAMILY COURT','South Shields County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('517400','953','SUNDERLAND COUNTY, FAMILY, MAGISTRATES AND TRIBUNAL HEARINGS','Sunderland County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('195537','954','TEESSIDE COMBINED COURT CENTRE','Teesside Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('195537','955','TEESSIDE COMBINED COURT CENTRE','Teesside Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('852649','956','WAKEFIELD CIVIL AND FAMILY JUSTICE CENTRE','Wakefield County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('107581','957','YORK COUNTY COURT AND FAMILY COURT','York County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('67542','958','YORK CROWN COURT','York Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('761518','961','BARROW-IN-FURNESS COUNTY COURT AND FAMILY COURT','Barrow in Furness County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('444097','962','BIRKENHEAD COUNTY COURT AND FAMILY COURT','Birkenhead County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('150431','963','BLACKBURN COUNTY COURT AND FAMILY COURT','Blackburn County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('336348','964','BLACKPOOL MAGISTRATES COURT','Blackpool County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('447533','965','BOLTON COMBINED COURT','Bolton Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('448747','967','BURNLEY COMBINED COURT CENTRE','Burnley Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('448747','968','BURNLEY COMBINED COURT CENTRE','Burnley Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('45106','970','CARLISLE COMBINED COURT','Carlisle Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('45106','971','CARLISLE COMBINED COURT','Carlisle Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('226511','972','CHESTER CIVIL AND FAMILY JUSTICE CENTRE','Chester County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('566296','973','CREWE (SOUTH CHESHIRE) MAGISTRATES COURT','Crewe County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('353615','975','LANCASTER MAGISTRATES COURT','Lancaster County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('345663','976','LIVERPOOL CIVIL AND FAMILY COURT','Liverpool County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('701411','978','MANCHESTER COUNTY AND FAMILY COURT','Manchester County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('232580','980','PRESTON COMBINED COURT CENTRE','Preston Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('563906','982','ST HELENS COUNTY COURT AND FAMILY COURT','St Helens County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('560788','983','STOCKPORT MAGISTRATES COURT AND FAMILY COURT','Stockport County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('209396','987','WEST CUMBRIA COURTHOUSE','West Cumbria County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('245068','988','WIGAN AND LEIGH MAGISTRATES COURT','Wigan County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('468040','989','CHESTER CROWN COURT','Chester Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('448077','990','LIVERPOOL CROWN COURT','Liverpool Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('144641','991','MANCHESTER CROWN COURT (CROWN SQUARE)','Manchester Crown Square Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('326944','992','MANCHESTER CROWN COURT (MINSHULL ST)','Manchester Minshull Street Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('409795','993','WARRINGTON COMBINED COURT','Warrington Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('827534','994','ABERYSTWYTH JUSTICE CENTRE','Aberystwyth Justice Centre - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('304576','995','BLACKWOOD CIVIL AND FAMILY COURT','Blackwood Civil and Family Centre - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('304576','996','BLACKWOOD CIVIL AND FAMILY COURT','Blackwood Civil and Family Centre - Family','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('366572','999','CAERNARFON  JUSTICE CENTRE','Caernarfon County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('366572','1000','CAERNARFON  JUSTICE CENTRE','Caernarfon Criminal Justice Centre - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('234850','1001','CARDIFF CIVIL AND FAMILY JUSTICE CENTRE','Cardiff Civil Justice Centre - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('234850','1002','CARDIFF CIVIL AND FAMILY JUSTICE CENTRE','Cardiff Civil Justice Centre - Family','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('302630','1003','CARDIFF CROWN COURT','Cardiff Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('101959','1004','CARMARTHEN COUNTY COURT AND FAMILY COURT','Carmarthen County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('101959','1005','CARMARTHEN COUNTY COURT AND FAMILY COURT','Carmarthen Law Courts - Family','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('700596','1008','HAVERFORDWEST COUNTY COURT AND FAMILY COURT','Haverfordwest County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('860090','1009','LLANDRINDOD WELLS MAGISTRATES AND FAMILY COURT','Llandrindod Wells Justice Centre - Family','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('390932','1010','LLANELLI  JUSTICE CENTRE','Llanelli County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('448345','1012','MERTHYR TYDFIL COMBINED COURT CENTRE','Merthyr Tydfil Combined Court - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('448345','1013','MERTHYR TYDFIL COMBINED COURT CENTRE','Merthyr Tydfil Combined Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('211138','1014','MOLD JUSTICE CENTRE','Mold Law Courts - Crown','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('211138','1015','MOLD JUSTICE CENTRE','Mold Law Courts - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('846055','1016','PORT TALBOT JUSTICE CENTRE','Neath & Port Talbot County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('217250','1017','NEWPORT (SOUTH WALES) COUNTY COURT AND FAMILY COURT','Newport Civil and Family Court - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('217250','1018','NEWPORT (SOUTH WALES) COUNTY COURT AND FAMILY COURT','Newport Civil and Family Court - Family','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('200518','1019','NEWPORT (SOUTH WALES) CROWN COURT','Newport Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('232298','1020','PONTYPRIDD COUNTY COURT AND FAMILY COURT','Pontypridd County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('846055','1021','PORT TALBOT JUSTICE CENTRE','Port Talbot Justice Centre - Family','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('234946','1023','SWANSEA CIVIL AND FAMILY JUSTICE CENTRE','Swansea Civil Justice Centre - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('234946','1024','SWANSEA CIVIL AND FAMILY JUSTICE CENTRE','Swansea Civil Justice Centre - Family','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('279152','1025','SWANSEA CROWN COURT','Swansea Crown Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('103147','1026','WELSHPOOL MAGISTRATES COURT','Welshpool County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('637145','1027','WREXHAM LAW COURTS','Wrexham Law Courts - County','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('','1030','','Immigration and Asylum First Tier','BFA1','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('','1032','','Social Entitlement','BBA3','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('416695','1060','HIGHBURY CORNER MAGISTRATES COURT','Highbury Corner Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('839746','1061','WESTMINSTER MAGISTRATES COURT','Westminster Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('549957','1063','WIMBLEDON MAGISTRATES COURT','Wimbledon Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('664444','1065','THAMES MAGISTRATES COURT','Thames Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('745389','1066','HENDON MAGISTRATES COURT','Hendon Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('514973','1067','STRATFORD MAGISTRATES COURT','Stratford Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('597501','1068','EALING MAGISTRATES COURT','Ealing Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('784131','1069','BROMLEY MAGISTRATES COURT','Bromley Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('747662','1070','CROYDON MAGISTRATES COURT','Croydon Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('536548','1071','LAVENDER HILL MAGISTRATES COURT (FORMERLY SOUTH WESTERN MAGISTRATES COURT)','Lavendar Hill Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('218723','1073','Barkingside Magistrates'' Court','Redbridge Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('264828','1074','UXBRIDGE MAGISTRATES COURT','Uxbridge Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('618632','1075','NOTTINGHAM MAGISTRATES COURT','Nottingham Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('703200','1076','WORCESTER MAGISTRATES COURT','Worcester Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('784730','1077','BIRMINGHAM MAGISTRATES COURT','Birmingham Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('417439','1078','LEICESTER MAGISTRATES COURT','Leicester Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('652852','1079','CHESTERFIELD MAGISTRATES','Chesterfield Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('370964','1080','NORTH STAFFORDSHIRE JUSTICE CENTRE','North Staffordshire Justice Centre - Magistrates','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('484482','1081','DERBY MAGISTRATES','Derby Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('590621','1082','WOLVERHAMPTON MAGISTRATES COURT','Wolverhampton Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('443257','1083','NORTH TYNESIDE MAGISTRATES COURT','North Tyneside Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('580554','1084','BRADFORD  AND KEIGHLEY MAGISTRATES COURT AND FAMILY COURT','Bradford Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('517400','1085','SUNDERLAND COUNTY, FAMILY, MAGISTRATES AND TRIBUNAL HEARINGS','Sunderland Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('641199','1086','GRIMSBY MAGISTRATES COURT AND FAMILY COURT','Grimsby Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('720624','1087','SHEFFIELD MAGISTRATES COURT','Sheffield Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('320113','1088','KIRKLEES (HUDDERSFIELD) MAGISTRATES COURT AND FAMILY COURT ','Kirklees Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('574546','1089','BARNSLEY LAW COURTS','Barnsley Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('449358','1090','TEESSIDE MAGISTRATES COURT','Teeside Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('569737','1091','LEEDS MAGISTRATES COURT AND FAMILY COURT','Leeds Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('362420','1093','HULL MAGISTRATES COURT','Hull Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('360566','1094','WIRRAL MAGISTRATES COURT','Wirral Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('336348','1095','BLACKPOOL MAGISTRATES COURT','Blackpool Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('783803','1096','MANCHESTER MAGISTRATES COURT','Manchester Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('243126','1097','CARLISLE MAGISTRATES COURT','Carlisle (Cumbria) Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('345663','1098','LIVERPOOL CIVIL AND FAMILY COURT','Liverpool Magistrates Court ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('215156','1099','BLACKBURN MAGISTRATES COURT','Blackburn Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('409795','1101','WARRINGTON COMBINED COURT','Warrington Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('568484','1102','GUILDFORD MAGISTRATES COURT AND FAMILY COURT','Guildford Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('659591','1103','MARGATE MAGISTRATES COURT','Margate Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('497356','1104','MILTON KEYNES MAGISTRATES COURT AND FAMILY COURT','Milton Keynes Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('287515','1105','IPSWICH MAGISTRATES COURT','Ipswich Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('624810','1106','CHELMSFORD MAGISTRATES COURT AND FAMILY COURT','Chelmsford Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('245287','1107','READING MAGISTRATES COURT AND FAMILY COURT','Reading Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('252292','1108','LUTON AND SOUTH BEDFORDSHIRE MAGISTRATES COURT AND FAMILY COURT','Luton Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('732661','1109','OXFORD AND SOUTHERN OXFORDSHIRE MAGISTRATES COURT','Oxford Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('625697','1110','ST ALBANS MAGISTRATES COURT ','St Albans Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('583034','1111','PETERBOROUGH MAGISTRATES COURT','Peterborough Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('417418','1112','BRIGHTON MAGISTRATES COURT','Brighton Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('777942','1113','NORWICH MAGISTRATES COURT AND FAMILY COURT','Norwich Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('224403','1114','SLOUGH COUNTY COURT AND FAMILY COURT','Slough Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('379247','1115','PORTSMOUTH MAGISTRATES COURT','Portsmouth Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('314074','1117','SWINDON MAGISTRATES COURT','Swindon Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('450049','1118','ALDERSHOT JUSTICE CENTRE','Aldershot Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('764728','1119','PLYMOUTH MAGISTRATES COURT','Plymouth Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('781155','1120','BRISTOL MAGISTRATES COURT','Bristol Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('330480','1121','WEST HAMPSHIRE MAGISTRATES COURT','Southampton Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('506742','1123','TAUNTON MAGISTRATES COURT, TRIBUNALS AND FAMILY HEARING CENTRE','Taunton Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('779109','1124','CARDIFF  MAGISTRATES COURT','Cardiff Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('211138','1125','MOLD JUSTICE CENTRE','Mold Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('324413','1126','NEWPORT (SOUTH WALES) MAGISTRATES COURT','Newport Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('296111','1127','CWMBRAN MAGISTRATES COURT','Cwmbran Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('20262','1147','ROYAL COURTS OF JUSTICE','Royal Courts of Justice - Senior Costs Office','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('427519','1156','GATESHEAD MAGISTRATES COURT AND FAMILY COURT','Gateshead Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('326813','1163','WALSALL MAGISTRATES COURT','Walsall Magistrate''s Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('602948','1164','NORTHAMPTON MAGISTRATES COURT','Northampton Magistrates Court ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('534157','1165','LINCOLN MAGISTRATES COURT','Lincoln Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('330480','1166','WEST HAMPSHIRE MAGISTRATES COURT','West Hampshire Magistrates Court ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('541183','1167','WILLESDEN MAGISTRATES COURT','Willesden Magistrates Court ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('787030','1168','COVENTRY MAGISTRATES COURT','Coventry Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('361595','1169','LLANDUDNO MAGISTRATES COURT','Llandudno Magistrates Court ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('218723','1170','BARKINGSIDE  MAGISTRATES COURT','Barkingside Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('478126','1171','GREAT GRIMSBY COMBINED COURT CENTRE','Grimsby County Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('371016','1172','OXFORD COMBINED COURT CENTRE','Oxford Combined Court Centre','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('819890','1173','BRISTOL CIVIL AND FAMILY JUSTICE CENTRE','Bristol Civil and Family Justice Centre','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('379656','1192','PRESTATYN JUSTICE CENTRE','Prestatyn Civil and Family Justice Centre','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('640119','1194','DONCASTER JUSTICE CENTRE NORTH','Doncaster Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('711798','1437','CHELTENHAM MAGISTRATES COURT','Cheltenham Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('270253','1445','NEWTON ABBOT MAGISTRATES COURT','Newton Abbot Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('771467','1446','MEDWAY MAGISTRATES COURT AND FAMILY COURT','Medway Magistrates Court','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
commit;

INSERT INTO judicial_service_code_mapping (service_id,ticket_code,service_code,service_description,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
(1,'368','BBA3','Social Security and Child Support','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(2,'364','BBA3','Social Security and Child Support','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(3,'362','BBA3','Social Security and Child Support','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(4,'365','BBA3','Social Security and Child Support','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(5,'366','BBA3','Social Security and Child Support','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(6,'367','BBA3','Social Security and Child Support','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(7,'369','BBA3','Social Security and Child Support','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(8,'290',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(9,'291',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(10,'387',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_service_code_mapping (service_id,ticket_code,service_code,service_description,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
(11,'318','BGA1','Agricultural Land and Drainage','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(12,'402','ABA2','Financial Remedy','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(13,'303',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(14,'320',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(15,'304',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(16,'321','BCA1','Care Standards','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(17,'305',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(18,'292',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(19,'293',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(20,'322','BAA2','Charity','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_service_code_mapping (service_id,ticket_code,service_code,service_description,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
(21,'363','BBA3','Social Security and Child Support','2022-04-04 00:00:00','2022-04-04 00:00:00','2022-11-04 22:00:00'),
(22,'407',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(23,'294','AAA1','Civil Enforcement','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(24,'294','AAA2','Insolvency','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(25,'294','AAA3','Mortgage and Landlord Possession Claims','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(26,'294','AAA4','Non-money Claims','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(27,'294','AAA5','Return of Goods Claims','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(28,'294','AAA6','Specified Money Claims','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(29,'294','AAA7','Damages','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(30,'295',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_service_code_mapping (service_id,ticket_code,service_code,service_description,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
(31,'325',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(32,'326','BAA4','Consumer Credit','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(33,'307',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(34,'313','ABA7','Court of Protections','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(35,'306',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(36,'328','BBA2','Criminal Injuries Compensation','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(37,'405','ABA3','Family Public Law','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(38,'405','ABA4','Adoption','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(39,'405','ABA5','Family Private Law','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(40,'405','ABA8','REMO','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_service_code_mapping (service_id,ticket_code,service_code,service_description,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
(41,'331','BAA5','Environment','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(42,'332','BAA6','Estate Agents','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(43,'308',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(44,'389','ABA3','Family Public Law','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(45,'389','ABA4','Adoption','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(46,'389','ABA5','Family Private Law','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(47,'389','ABA8','REMO','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(48,'410','ABA2','Financial Remedy','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(49,'409',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(50,'314','ABA2','Financial Remedy','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_service_code_mapping (service_id,ticket_code,service_code,service_description,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
(51,'392',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(52,'374',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(53,'373','BFA1','Immigration and Asylum Appeals','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(54,'375',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(55,'376','BBA3','Social Security and Child Support','2022-04-04 00:00:00','2022-04-04 00:00:00','2022-11-04 22:00:00'),
(56,'377',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(57,'378','BEA1','War Pensions Appeals','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(58,'395',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(59,'334','BAA8','Gambling Appeals','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(60,'336','BAB1','Information Rights','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_service_code_mapping (service_id,ticket_code,service_code,service_description,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
(61,'337',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(62,'340','BGA2','Land Registration','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(63,'341','BAB2','Local Government Standards','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(64,'406',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(65,'412',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(66,'342','BCA2','Mental Health','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(67,'343','BCA2','Mental Health','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(68,'298',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(69,'309',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(70,'346',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_service_code_mapping (service_id,ticket_code,service_code,service_description,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
(71,'404',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(72,'379','BHA1','Employment Claims','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(73,'381',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(74,'382',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(75,'383',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(76,'384','BBA1','Asylum Support','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(77,'299',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(78,'347',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(79,'350',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(80,'394',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_service_code_mapping (service_id,ticket_code,service_code,service_description,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
(81,'349','BCA3','Primary Health Lists','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(82,'315','ABA5','Family Private Law','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(83,'316','ABA3','Family Public Law','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(84,'351',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(85,'352',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(86,'353','BHA3','Reserve Forces Appeal Tribunal','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(87,'354','BGA3','Residential Property','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(88,'356',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(89,'397',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(90,'400',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_service_code_mapping (service_id,ticket_code,service_code,service_description,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
(91,'398',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(92,'317',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(93,'300',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(94,'396',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(95,'301',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(96,'393',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(97,'311',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(98,'312',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(99,'408',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(100,'357','BBA3','Social Security and Child Support','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_service_code_mapping (service_id,ticket_code,service_code,service_description,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
(101,'358',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(102,'411',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(103,'302',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(104,'310',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(105,'359',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(106,'360','BAB3','Transport','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(107,'370',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(108,'371',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(109,'372','BAA9','Immigration Services','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(110,'385',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_service_code_mapping (service_id,ticket_code,service_code,service_description,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
(111,'386','BDA2','Tax Appeals','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(112,'289',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(113,'388',' ','','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
(114,'1416','BBA3','Social Security and Child Support','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);

	COMMIT;
INSERT INTO jrd_lrd_region_mapping (jrd_region_id,jrd_region,region_id,mrd_created_time,mrd_updated_time,mrd_deleted_time,region) VALUES
('1','National','1','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'National'),
('2','National England and Wales','1','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'National'),
('3','Taylor House (London)','2','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'London'),
('4','Hatton Cross (London)','2','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'London'),
('5','Newport (Wales)','8','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'Wales'),
('6','Glasgow (Scotland and NI)','9','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'Scotland'),
('7','Birmingham','3','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'Midlands'),
('8','North Shields','4','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'North East'),
('9','Stoke','3','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'Midlands'),
('10','Manchester','5','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'North west'),
('11','Bradford','4','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'North East'),
('12','Nottingham','3','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'Midlands'),
('13','Field House (London)','2','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'London'),
('14','London','2','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'London'),
('15','London Central','2','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'London'),
('16','London East','2','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'London'),
('17','London South','2','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'London'),
('18','South East','6','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'South east'),
('19','South Eastern','6','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'South east'),
('20','Midlands','3','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'Midlands'),
('21','Midlands East','3','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'Midlands'),
('22','Midlands West','3','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'Midlands'),
('23','South West','7','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'South west'),
('24','South Western','7','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'South west'),
('25','North West','5','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'North west'),
('26','North East','4','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'North East'),
('27','Wales','8','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'Wales'),
('28','Scotland','9','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'Scotland'),
('29','Northern','','2022-04-04 00:00:00','2022-04-04 00:00:00','2022-11-04 22:00:00','NULL'),
('30','Southern','','2022-04-04 00:00:00','2022-04-04 00:00:00','2022-11-04 22:00:00','NULL'),
('31','Eastern','','2022-04-04 00:00:00','2022-04-04 00:00:00','2022-11-04 22:00:00','NULL'),
('32','Yorkshire and Humberside','4','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'North East'),
('33','Newcastle','4','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'North East'),
('34','also sits in Conduct Committee','','2022-04-04 00:00:00','2022-04-04 00:00:00','2022-11-04 22:00:00','NULL'),
('35','EAT - Rolls Building','2','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL,'London');
commit;

INSERT INTO judicial_ticket_code_mapping (ticket_code,jurisdiction,lower_level,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('368','Authorisation Tribunals','00 - Interlocutory','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('364','Authorisation Tribunals','01 - Social Security','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('362','Authorisation Tribunals','02 - Child Support','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('365','Authorisation Tribunals','03 - Disability Living Allowance','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('366','Authorisation Tribunals','04 - Incapacity Benefit Employment Support','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('367','Authorisation Tribunals','05 - Industrial Injuries','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('369','Authorisation Tribunals','07 - Vaccine Damage','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('290','Authorisation Civil','Administrative Court','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('291','Authorisation Civil','Admiralty - QBD','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('387','Authorisation Magistrate','Adult Court','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_ticket_code_mapping (ticket_code,jurisdiction,lower_level,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('318','Authorisation Tribunals','Agricultural Land and Drainage','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('402','Authorisation Family','Ancillary Relief Appeals','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('303','Authorisation Crime','Appeals in Crown Court','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('320','Authorisation Tribunals','Asylum Support','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('304','Authorisation Crime','Attempted Murder','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('321','Authorisation Tribunals','Care Standards','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('305','Authorisation Crime','Central Criminal Court','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('292','Authorisation Civil','Chancery','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('293','Authorisation Civil','Chancery Business in the County Court','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('322','Authorisation Tribunals','Charity','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_ticket_code_mapping (ticket_code,jurisdiction,lower_level,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('363','Authorisation Tribunals','Child Support 02','2022-04-04 00:00:00','2022-04-04 00:00:00','2022-11-04 22:00:00'),
('407','Authorisation Civil','Circuit Commercial Court','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('294','Authorisation Civil','Civil Authorisation','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('295','Authorisation Civil','Commercial','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('325','Authorisation Tribunals','Competition Appeal Tribunal','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('326','Authorisation Tribunals','Consumer Credit','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('307','Authorisation Crime','Court of Appeal Criminal Division','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('313','Authorisation Family','Court of Protection','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('306','Authorisation Crime','Criminal Authorisation','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('328','Authorisation Tribunals','Criminal Injuries Compensations','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_ticket_code_mapping (ticket_code,jurisdiction,lower_level,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('405','Authorisation Magistrate','Direct Recruitment to Family','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('331','Authorisation Tribunals','Environment','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('332','Authorisation Tribunals','Estate Agents','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('308','Authorisation Crime','Extradition','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('389','Authorisation Magistrate','Family Court','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('410','Authorisation Family','Financial Remedy','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('409','Authorisation Civil','Financial Remedy ','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('314','Authorisation Family','Financial Remedy Appeals','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('392','Authorisation Tribunals','First Tier - General Regulatory','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('374','Authorisation Tribunals','First Tier - Health, Education and Social Care','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_ticket_code_mapping (ticket_code,jurisdiction,lower_level,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('373','Authorisation Tribunals','First Tier - Immigration and Asylum','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('375','Authorisation Tribunals','First Tier - Property','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('376','Authorisation Tribunals','First Tier - Social Entitlement','2022-04-04 00:00:00','2022-04-04 00:00:00','2022-11-04 22:00:00'),
('377','Authorisation Tribunals','First Tier - Tax','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('378','Authorisation Tribunals','First Tier - War Pensions and Armed Forces Compensation','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('395','Authorisation Civil','Freezing Orders in the County Court','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('334','Authorisation Tribunals','Gambling','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('336','Authorisation Tribunals','Information Rights','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('337','Authorisation Tribunals','Judicial Review - England and Wales','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('340','Authorisation Tribunals','Land Registration','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_ticket_code_mapping (ticket_code,jurisdiction,lower_level,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('341','Authorisation Tribunals','Local Government Standards - England','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('406','Authorisation Civil','London Circuit Commercial Court','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('412','Authorisation Civil','Media and Communications List','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('342','Authorisation Tribunals','Mental Health','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('343','Authorisation Tribunals','Mental Health Tribunal Wales','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('298','Authorisation Civil','Mercantile - QBD','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('309','Authorisation Crime','Murder','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('346','Authorisation Tribunals','National Security','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('404','Authorisation Civil','Ordinary Planning','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('379','Authorisation Tribunals','Others - Employment Appeals Tribunal','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_ticket_code_mapping (ticket_code,jurisdiction,lower_level,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('381','Authorisation Tribunals','Others - Gender Recognition Panel','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('382','Authorisation Tribunals','Others - Pathogens Access Appeals Commission','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('383','Authorisation Tribunals','Others - Proscribed Organisations Appeal Commission','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('384','Authorisation Tribunals','Others - Special Immigration Appeals Commission','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('299','Authorisation Civil','Patents','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('347','Authorisation Tribunals','Pensions Regulations','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('350','Authorisation Tribunals','Police Appeal Tribunal','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('394','Authorisation Crime','Pool of Judges','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('349','Authorisation Tribunals','Primary Health List','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('315','Authorisation Family','Private Law','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_ticket_code_mapping (ticket_code,jurisdiction,lower_level,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('316','Authorisation Family','Public Law','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('351','Authorisation Tribunals','Race Panel','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('352','Authorisation Tribunals','Reinstatement Committee','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('353','Authorisation Tribunals','Reserve Forces','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('354','Authorisation Tribunals','Residential Property','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('356','Authorisation Tribunals','Restricted Patients Panel','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('397','Authorisation Civil','S9(4) Appointment - Chancery','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('400','Authorisation Family','S9(4) Appointment - Family','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('398','Authorisation Civil','S9(4) Appointment - Queens Bench','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('317','Authorisation Family','Section 9-1 Family','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_ticket_code_mapping (ticket_code,jurisdiction,lower_level,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('300','Authorisation Civil','Section 9(1) Chancery','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('396','Authorisation Civil','Section 9(1) IPEC','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('301','Authorisation Civil','Section 9(1) Queens Bench','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('393','Authorisation Civil','Section 9(1) Queens Bench - Admin ONLY','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('311','Authorisation Crime','Serious Sexual Offences','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('312','Authorisation Crime','Serious Sexual Offences - Youth Court','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('408','Authorisation Civil','Significant Planning','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('357','Authorisation Tribunals','Social Security and Child Support','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('358','Authorisation Tribunals','Special Educational Needs and Disability','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('411','Authorisation Civil','Super Planning','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_ticket_code_mapping (ticket_code,jurisdiction,lower_level,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('302','Authorisation Civil','Technology and Construction Court','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('310','Authorisation Crime','Terrorism','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('359','Authorisation Tribunals','Trade Marks Tribunal','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('360','Authorisation Tribunals','Transport','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('370','Unknown','Unknown','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('371','Authorisation Tribunals','Upper - Administrative Appeals','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('372','Authorisation Tribunals','Upper - Immigration and Asylum','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('385','Authorisation Tribunals','Upper - Lands','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('386','Authorisation Tribunals','Upper - Tax and Chancery','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('289','Languages','Welsh','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL);
INSERT INTO judicial_ticket_code_mapping (ticket_code,jurisdiction,lower_level,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('388','Authorisation Magistrate','Youth Court','2022-04-04 00:00:00','2022-04-04 00:00:00',NULL),
('1416','Authorisation Tribunals','06 - Industrial injuries 2','2022-11-04 22:00:00','2022-11-04 22:00:00',NULL);

UPDATE judicial_location_mapping  SET service_code = NULL WHERE service_code = '';

INSERT INTO judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code,mrd_created_time,mrd_updated_time,mrd_deleted_time) VALUES
('736719','916','LEAMINGTON SPA MAGISTRATES'' COURT','Warwick Combined Court - County',NULL,'2022-05-16 00:00:00','2022-05-16 00:00:00',NULL),
('232580','981','PRESTON COMBINED COURT CENTRE','Preston Combined Court - Crown',NULL,'2022-05-16 00:00:00','2022-05-16 00:00:00',NULL),
('415903','1191','LEYLAND FAMILY COURT','Leyland Family Hearing Centre',NULL,'2022-05-16 00:00:00','2022-05-16 00:00:00',NULL),
('450049','1447','ALDERSHOT JUSTICE CENTRE','Aldershot Justice Centre - Crown',NULL,'2022-05-16 00:00:00','2022-05-16 00:00:00',NULL),
('450049','1448','ALDERSHOT JUSTICE CENTRE','Aldershot Justice Centre - County',NULL,'2022-05-16 00:00:00','2022-05-16 00:00:00',NULL),
('640119','1449','DONCASTER JUSTICE CENTRE NORTH','Doncaster Justice Centre North',NULL,'2022-05-16 00:00:00','2022-05-16 00:00:00',NULL),
('45900','1450','DONCASTER JUSTICE CENTRE SOUTH','Doncaster Justice Centre South',NULL,'2022-05-16 00:00:00','2022-05-16 00:00:00',NULL),
('490237','1451','POOLE MAGISTRATES COURT','Poole Magistrates Court',NULL,'2022-05-16 00:00:00','2022-05-16 00:00:00',NULL),
('366796','1452','NEWCASTLE CIVIL & FAMILY COURTS AND TRIBUNALS CENTRE','Newcastle Civil & Family Courts & Tribunals Centre',NULL,'2022-05-16 00:00:00','2022-05-16 00:00:00',NULL);
COMMIT;