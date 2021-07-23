---- Create table script : jrd_lrd_region_mapping
CREATE TABLE jrd_lrd_region_mapping(
    jrd_region_id VARCHAR(16) NOT NULL,
    jrd_region VARCHAR(256) NOT NULL,
    region_id VARCHAR(16) NOT NULL,
    region VARCHAR(256) NOT NULL
);

---- Inserts script : jrd_lrd_region_mapping
INSERT INTO jrd_lrd_region_mapping
(jrd_region_id, jrd_region, region_id, region)
VALUES('1', 'National', '1', 'National'),
('2', 'National England and Wales', '1', 'National'),
('3', 'Taylor House (London)', '2', 'London'),
('4', 'Hatton Cross (London)', '2', 'London'),
('5', 'Newport (Wales)', '8', 'Wales'),
('6', 'Glasgow (Scotland and NI)', '9', 'Scotland'),
('7', 'Birmingham', '3', 'Midlands'),
('8', 'North Shields', '4', 'North East'),
('9', 'Stoke', '3', 'Midlands'),
('10', 'Manchester', '5', 'North west'),
('11', 'Bradford', '4', 'North East'),
('12', 'Nottingham', '3', 'Midlands'),
('13', 'Field House (London)', '2', 'London'),
('14', 'London', '2', 'London'),
('15', 'London Central', '2', 'London'),
('16', 'London East', '2', 'London'),
('17', 'London South', '2', 'London'),
('18', 'South East', '6', 'South east'),
('19', 'South Eastern', '6', 'South east'),
('20', 'Midlands', '3', 'Midlands'),
('21', 'Midlands East', '3', 'Midlands'),
('22', 'Midlands West', '3', 'Midlands'),
('23', 'South West', '7', 'South west'),
('24', 'South Western', '7', 'South west'),
('25', 'North West', '5', 'North west'),
('26', 'North East', '4', 'North East'),
('27', 'Wales', '8', 'Wales'),
('28', 'Scotland', '9', 'Scotland'),
('32', 'Yorkshire and Humberside', '4', 'North East'),
('33', 'Newcastle', '4', 'North East'),
('35', 'EAT - Rolls Building', '2', 'London');