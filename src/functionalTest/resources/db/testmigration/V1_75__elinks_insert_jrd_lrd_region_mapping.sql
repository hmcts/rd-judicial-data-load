INSERT INTO dbjudicialdata.jrd_lrd_region_mapping (jrd_region_id,jrd_region,region_id,region) VALUES
    ('1719',    'East of England',  '5',    'South East'),
    ('2213',	'West Mercia',      '2',	'Midlands'),
    ('2175',	'Northumbria',      '3',	'North East'),
    ('1959',	'Southern',         '6',	'South West'),
    ('1516',	'Eastern',          '5',	'South East');

INSERT INTO judicial_service_code_mapping (service_id,ticket_code,service_code,service_description) VALUES ('122','380','BHA1','Employment Claims');
INSERT INTO judicial_service_code_mapping (service_id,ticket_code,service_code,service_description) VALUES ('123','1413','BHA1','Employment Claims');

INSERT INTO judicial_ticket_code_mapping (ticket_code,jurisdiction,lower_level)
VALUES ('380', 'Authorisation Tribunals', 'Others - Employment Tribunal (Scotland)');
INSERT INTO judicial_ticket_code_mapping (ticket_code,jurisdiction,lower_level)
VALUES ('1413', 'Authorisation Tribunals','Others - Employment Tribunal England & Wales');