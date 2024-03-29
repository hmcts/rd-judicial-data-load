
--Alter dbjudicialdata.dataload_exception_records
ALTER TABLE dbjudicialdata.dataload_exception_records ALTER COLUMN row_id TYPE varchar(32);


-- insert dbjudicialdata.judicial_ticket_code_type
INSERT INTO dbjudicialdata.judicial_ticket_code_type (ticket_code, ticket_category_id, lower_level) VALUES
      ('289',	'59',	'Welsh Language'),
      ('290',	'24',	'Administrative Court'),
      ('291',	'24',	'Admiralty - KBD'),
      ('292',	'24',	'Chancery'),
      ('293',	'24',	'Chancery Business in the County Court'),
      ('294',	'24',	'Civil Authorisation'),
      ('295',	'24',	'Commercial'),
      ('296',	'24',	'Companies Court'),
      ('298',	'24',	'Mercantile - KBD'),
      ('299',	'24',	'Patents'),
      ('300',	'24',	'Section 9(1) Chancery'),
      ('301',	'24',	'Section 9(1) Kings Bench'),
      ('302',	'24',	'Technology and Construction Court'),
      ('303',	'25',	'Appeals in Crown Court'),
      ('304',	'25',	'Attempted Murder'),
      ('305',	'25',	'Central Criminal Court'),
      ('306',	'25',	'Criminal Authorisation'),
      ('307',	'25',	'Court of Appeal Criminal Division'),
      ('308',	'25',	'Extradition'),
      ('309',	'25',	'Murder'),
      ('310',	'25',	'Terrorism'),
      ('311',	'25',	'Serious Sexual Offences'),
      ('312',	'25',	'Serious Sexual Offences - Youth Court'),
      ('313',	'26',	'Court of Protection'),
      ('314',	'26',	'Financial Remedy Appeals'),
      ('315',	'26',	'Private Law'),
      ('316',	'26',	'Public Law'),
      ('317',	'26',	'Section 9-1 Family'),
      ('318',	'16',	'Agricultural Land and Drainage'),
      ('319',	'16',	'Agricultural Lands Tribunal Wales'),
      ('320',	'10',	'Asylum Support'),
      ('321',	'11',	'Care Standards'),
      ('322',	'13',	'Charity'),
      ('323',	'13',	'Claims Management Services'),
      ('324',	'16',	'Community Rights to Bid'),
      ('325',	'103',	'Competition Appeal Tribunal'),
      ('326',	'13',	'Consumer Credit'),
      ('327',	'105',	'Copyright Tribunal'),
      ('328',	'10',	'Criminal Injuries Compensations'),
      ('329',	'106',	'Design Tribunal'),
      ('330',	'14',	'Direct and Indirect Taxation'),
      ('331',	'13',	'Environment'),
      ('332',	'13',	'Estate Agents'),
      ('333',	'13',	'Food'),
      ('334',	'13',	'Gambling'),
      ('335',	'13',	'Immigration Services'),
      ('336',	'13',	'Information Rights'),
      ('337',	'21',	'Judicial Review - England and Wales'),
      ('338',	'21',	'Judicial Review - Northern Ireland'),
      ('339',	'21',	'Judicial Review - Scotland'),
      ('340',	'16',	'Land Registration'),
      ('341',	'13',	'Local Government Standards - England'),
      ('342',	'11',	'Mental Health'),
      ('343',	'11',	'Mental Health Tribunal Wales'),
      ('344',	'42',	'Motor Insurers Bureau'),
      ('345',	'14',	'MP''s Expenses'),
      ('346',	'13',	'National Security'),
      ('347',	'13',	'Pensions Regulations'),
      ('348',	'13',	'Professional Regulations'),
      ('349',	'11',	'Primary Health List'),
      ('350',	'45',	'Police Appeals Tribunal'),
      ('351',	'13',	'Race Panel'),
      ('352',	'47',	'Reinstatement Committee'),
      ('353',	'15',	'Reserve Forces'),
      ('354',	'16',	'Residential Property'),
      ('355',	'49',	'Residential Property Tribunal Wales'),
      ('356',	'11',	'Restricted Patients Panel'),
      ('357',	'10',	'Social Security and Child Support'),
      ('358',	'11',	'Special Educational Needs and Disability'),
      ('359',	'54',	'Trademark Tribunals'),
      ('360',	'55',	'Transport'),
      ('361',	'56',	'Valuation Tribunal England'),
      ('362',	'10',	'02 - Child Support'),
      ('364',	'10',	'01 - Social Security'),
      ('365',	'10',	'03 - Disability Living Allowance'),
      ('366',	'10',	'04 - Incapacity Benefit Employment Support'),
      ('367',	'10',	'05 - Industrial Injuries'),
      ('368',	'10',	'00 - Interlocutory'),
      ('369',	'10',	'07 - Vaccine Damage'),
      ('371',	'18',	'Upper - Administrative Appeals'),
      ('372',	'19',	'Upper - Immigration and Asylum'),
      ('373',	'12',	'First Tier - Immigration and Asylum'),
      ('374',	'11',	'First Tier - Health, Education and Social Care'),
      ('375',	'16',	'First Tier - Property'),
      ('376',	'10',	'First Tier - Social Entitlement'),
      ('377',	'14',	'First Tier - Tax'),
      ('378',	'15',	'First Tier - War Pensions and Armed Forces Compensation'),
      ('379',	'32',	'Employment Appeal Tribunal'),
      ('380',	'31',	'Employment Tribunal (Scotland)'),
      ('381',	'33',	'Others - Gender Recognition Panel'),
      ('382',	'43',	'Others - Pathogens Access Appeals Commission'),
      ('383',	'46',	'Others - Proscribed Organisations Appeal Commission'),
      ('384',	'52',	'Others - Special Immigration Appeals Commission'),
      ('385',	'20',	'Upper - Lands'),
      ('386',	'21',	'Upper - Tax and Chancery'),
      ('387',	'58',	'Adult Court'),
      ('388',	'58',	'Youth Court'),
      ('389',	'58',	'Family Court'),
      ('392',	'13',	'First Tier - General Regulatory'),
      ('393',	'24',	'Section 9(1) Kings Bench - Admin ONLY'),
      ('394',	'25',	'Pool of Judges'),
      ('395',	'24',	'Freezing Orders in the County Court'),
      ('396',	'24',	'IPEC'),
      ('397',	'24',	'S9(4) Appointment - Chancery'),
      ('398',	'24',	'S9(4) Appointment - Kings Bench'),
      ('400',	'26',	'S9(4) Appointment - Family'),
      ('401',	'25',	'Murder - Deputy Circuit Judge Only'),
      ('402',	'26',	'Ancillary Relief Appeals'),
      ('403',	'24',	'District Judge in the County Court'),
      ('404',	'24',	'Ordinary Planning'),
      ('405',	'58',	'Direct Recruitment to Family'),
      ('406',	'24',	'London Circuit Commercial Court'),
      ('407',	'24',	'Circuit Commercial Court'),
      ('408',	'24',	'Significant Planning'),
      ('409',	'24',	'Financial Remedy'),
      ('410',	'26',	'Financial Remedy'),
      ('411',	'24',	'Super Planning'),
      ('412',	'24',	'Media and Communications List'),
      ('1412',	'24',	'Election Rota'),
      ('1413',	'30',	'Employment Tribunal (England & Wales)'),
      ('1415',	'24',	'Civil Authorisation (High Court Judge Chancery Division)'),
      ('1416',	'10',	'06 - Industrial injuries 2'),
      ('1417',	'24',	'Financial List'),
      ('1450',	'58',	'Adult Crime'),
      ('1451',	'58',	'Adult Crime Presiding Justice'),
      ('1452',	'58',	'Family Winger'),
      ('1453',	'58',	'Family Presiding Justice'),
      ('1454',	'58',	'Youth Winger'),
      ('1455',	'58',	'Youth Presiding Justice');