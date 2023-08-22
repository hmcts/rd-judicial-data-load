
CREATE TABLE dbjudicialdata.judicial_service_code_mapping (
	service_id bigint NOT NULL,
	ticket_code varchar(16) NOT NULL,
	service_code varchar(64) NULL,
	service_description varchar(512) NULL,
	CONSTRAINT service_id PRIMARY KEY (service_id)
);


INSERT INTO dbjudicialdata.judicial_service_code_mapping (service_id,ticket_code,service_code,service_description) VALUES
	 (1,'368','BBA3','Social Security and Child Support'),
	 (2,'364','BBA3','Social Security and Child Support'),
	 (3,'362','BBA3','Social Security and Child Support'),
	 (4,'365','BBA3','Social Security and Child Support'),
	 (5,'366','BBA3','Social Security and Child Support'),
	 (6,'367','BBA3','Social Security and Child Support'),
	 (7,'369','BBA3','Social Security and Child Support'),
	 (8,'290',' ',''),
	 (9,'291',' ',''),
	 (10,'387',' ','');
INSERT INTO dbjudicialdata.judicial_service_code_mapping (service_id,ticket_code,service_code,service_description) VALUES
	 (11,'318','BGA1','Agricultural Land and Drainage'),
	 (13,'303',' ',''),
	 (14,'320',' ',''),
	 (15,'304',' ',''),
	 (16,'321','BCA1','Care Standards'),
	 (17,'305',' ',''),
	 (18,'292',' ',''),
	 (19,'293',' ',''),
	 (20,'322','BAA2','Charity'),
	 (22,'407',' ','');
INSERT INTO dbjudicialdata.judicial_service_code_mapping (service_id,ticket_code,service_code,service_description) VALUES
	 (23,'294','AAA1','Civil Enforcement'),
	 (24,'294','AAA2','Insolvency'),
	 (25,'294','AAA3','Mortgage and Landlord Possession Claims'),
	 (26,'294','AAA4','Non-money Claims'),
	 (27,'294','AAA5','Return of Goods Claims'),
	 (28,'294','AAA6','Specified Money Claims'),
	 (29,'294','AAA7','Damages'),
	 (30,'295',' ',''),
	 (31,'325',' ',''),
	 (32,'326','BAA4','Consumer Credit');
INSERT INTO dbjudicialdata.judicial_service_code_mapping (service_id,ticket_code,service_code,service_description) VALUES
	 (33,'307',' ',''),
	 (34,'313','ABA7','Court of Protections'),
	 (35,'306',' ',''),
	 (36,'328','BBA2','Criminal Injuries Compensation'),
	 (40,'405','ABA8','REMO'),
	 (41,'331','BAA5','Environment'),
	 (42,'332','BAA6','Estate Agents'),
	 (43,'308',' ',''),
	 (44,'389','ABA3','Family Public Law'),
	 (45,'389','ABA4','Adoption');
INSERT INTO dbjudicialdata.judicial_service_code_mapping (service_id,ticket_code,service_code,service_description) VALUES
	 (46,'389','ABA5','Family Private Law'),
	 (47,'389','ABA8','REMO'),
	 (48,'410','ABA2','Financial Remedy'),
	 (49,'409',' ',''),
	 (51,'392',' ',''),
	 (52,'374',' ',''),
	 (53,'373','BFA1','Immigration and Asylum Appeals'),
	 (54,'375',' ',''),
	 (56,'377',' ',''),
	 (57,'378','BEA1','War Pensions Appeals');
INSERT INTO dbjudicialdata.judicial_service_code_mapping (service_id,ticket_code,service_code,service_description) VALUES
	 (58,'395',' ',''),
	 (59,'334','BAA8','Gambling Appeals'),
	 (60,'336','BAB1','Information Rights'),
	 (61,'337',' ',''),
	 (62,'340','BGA2','Land Registration'),
	 (63,'341','BAB2','Local Government Standards'),
	 (64,'406',' ',''),
	 (65,'412',' ',''),
	 (66,'342','BCA2','Mental Health'),
	 (67,'343','BCA2','Mental Health');
INSERT INTO dbjudicialdata.judicial_service_code_mapping (service_id,ticket_code,service_code,service_description) VALUES
	 (68,'298',' ',''),
	 (69,'309',' ',''),
	 (70,'346',' ',''),
	 (71,'404',' ',''),
	 (72,'379','BHA1','Employment Claims'),
	 (73,'381',' ',''),
	 (74,'382',' ',''),
	 (75,'383',' ',''),
	 (76,'384','BBA1','Asylum Support'),
	 (77,'299',' ','');
INSERT INTO dbjudicialdata.judicial_service_code_mapping (service_id,ticket_code,service_code,service_description) VALUES
	 (78,'347',' ',''),
	 (79,'350',' ',''),
	 (80,'394',' ',''),
	 (81,'349','BCA3','Primary Health Lists'),
	 (82,'315','ABA5','Family Private Law'),
	 (83,'316','ABA3','Family Public Law'),
	 (84,'351',' ',''),
	 (85,'352',' ',''),
	 (86,'353','BHA3','Reserve Forces Appeal Tribunal'),
	 (87,'354','BGA3','Residential Property');
INSERT INTO dbjudicialdata.judicial_service_code_mapping (service_id,ticket_code,service_code,service_description) VALUES
	 (88,'356',' ',''),
	 (89,'397',' ',''),
	 (91,'398',' ',''),
	 (93,'300',' ',''),
	 (94,'396',' ',''),
	 (95,'301',' ',''),
	 (96,'393',' ',''),
	 (97,'311',' ',''),
	 (98,'312',' ',''),
	 (99,'408',' ','');
INSERT INTO dbjudicialdata.judicial_service_code_mapping (service_id,ticket_code,service_code,service_description) VALUES
	 (100,'357','BBA3','Social Security and Child Support'),
	 (101,'358',' ',''),
	 (102,'411',' ',''),
	 (103,'302',' ',''),
	 (104,'310',' ',''),
	 (105,'359',' ',''),
	 (106,'360','BAB3','Transport'),
	 (107,'370',' ',''),
	 (108,'371',' ',''),
	 (109,'372','BAA9','Immigration Services');
INSERT INTO dbjudicialdata.judicial_service_code_mapping (service_id,ticket_code,service_code,service_description) VALUES
	 (110,'385',' ',''),
	 (111,'386','BDA2','Tax Appeals'),
	 (112,'289',' ',''),
	 (92,'317','ABA5','Family Private Law'),
	 (90,'400','ABA5','Family Private Law'),
	 (113,'388',' ',''),
	 (114,'1416','BBA3','Social Security and Child Support'),
	 (115,'400','ABA3','Family Public Law'),
	 (116,'317','ABA3','Family Public Law'),
	 (117,'374','BCA1','Care Standards');
INSERT INTO dbjudicialdata.judicial_service_code_mapping (service_id,ticket_code,service_code,service_description) VALUES
	 (118,'374','BCA2','Mental Health'),
	 (119,'374','BCA3','Primary Health Lists'),
	 (120,'374','BCA4','Special Educational Needs'),
	 (121,'376','BBA2','Criminal Injuries Compensation');
