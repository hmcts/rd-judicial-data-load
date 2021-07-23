create table judicial_service_code_mapping(
        service_code varchar(16),
        jurisdiction varchar(256),
        lower_level varchar(256),
        service_description varchar(512),
        CONSTRAINT service_code UNIQUE (service_code)
);

create table judicial_location_mapping(
        epimms_id varchar(16),
        judicial_base_location_id varchar(64),
        building_location_name varchar(256),
        base_location_name varchar(128)
);

insert into judicial_service_code_mapping(
       service_code,
       jurisdiction,
       lower_level,
       service_description)
values(
        'BFA1',
        'Authorisation Tribunals',
        'First Tier - Immigration and Asylum',
        'Immigration and Asylum Appeals')
;

insert into judicial_location_mapping(
        epimms_id,
        judicial_base_location_id,
        building_location_name,
        base_location_name)
values(
        '227101',
        '1030',
        'NEWPORT TRIBUNAL CENTRE - COLUMBUS HOUSE',
        'Immigration and Asylum First Tier'),
        (
        '231596',
        '1030',
        'BIRMINGHAM CIVIL AND FAMILY JUSTICE CENTRE',
        'Immigration and Asylum First Tier'
        ),
        (
        '366559',
        '1030',
        'ATLANTIC QUAY GLASGOW',
        'Immigration and Asylum First Tier'
        ),
        (
        '366796',
        '1030',
        'NEWCASTLE CIVIL & FAMILY COURTS AND TRIBUNALS CENTRE',
        'Immigration and Asylum First Tier'
        ),
        (
        '386417',
        '1030',
        'HATTON CROSS TRIBUNAL HEARING CENTRE',
        'Immigration and Asylum First Tier'
        ),
        (
        '512401',
        '1030',
        'MANCHESTER TRIBUNAL HEARING CENTRE - PICCADILLY EXCHANGE',
        'Immigration and Asylum First Tier'
        ),
        (
        '698118',
        '1030',
        'BRADFORD TRIBUNAL HEARING CENTRE',
        'Immigration and Asylum First Tier'
        ),
        (
        '765324',
        '1030',
        'TAYLOR HOUSE TRIBUNAL HEARING CENTRE',
        'Immigration and Asylum First Tier')
;
