--Alter dbjudicialdata.judicial_user_profile

ALTER TABLE dbjudicialdata.judicial_user_profile ADD COLUMN title varchar(64);
ALTER TABLE dbjudicialdata.judicial_user_profile ADD COLUMN retirement_date date ;
ALTER TABLE dbjudicialdata.judicial_user_profile ADD COLUMN leaving_on  date;


--Alter dbjudicialdata.judicial_office_appointment
ALTER TABLE dbjudicialdata.judicial_office_appointment ADD COLUMN appointment_id  varchar(256) not null;
ALTER TABLE dbjudicialdata.judicial_office_appointment ADD COLUMN role_name_id  varchar(256);
ALTER TABLE dbjudicialdata.judicial_office_appointment ADD COLUMN type  varchar(64);
ALTER TABLE dbjudicialdata.judicial_office_appointment ADD COLUMN contract_type_id  varchar(64);
ALTER TABLE dbjudicialdata.judicial_office_appointment ADD COLUMN location  varchar(64);


--Alter dbjudicialdata.judicial_office_authorisation
ALTER TABLE dbjudicialdata.judicial_office_authorisation ADD COLUMN appointment_id  varchar(256);
ALTER TABLE dbjudicialdata.judicial_office_authorisation ADD COLUMN authorisation_id  varchar(256) not null;
ALTER TABLE dbjudicialdata.judicial_office_authorisation ADD COLUMN jurisdiction_id  varchar(64) not null;


--Alter dbjudicialdata.judicial_role_type
ALTER TABLE dbjudicialdata.judicial_role_type ADD COLUMN jurisdiction_role_id  varchar(64) not null;
