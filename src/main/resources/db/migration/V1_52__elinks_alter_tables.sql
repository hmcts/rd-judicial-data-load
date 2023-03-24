--Create sequence for dbjudicialdata.judicial_office_authorisation
CREATE SEQUENCE dbjudicialdata.judicial_office_auth_id_sequence AS integer START 1 OWNED
BY dbjudicialdata.judicial_office_authorisation.judicial_office_auth_id;

ALTER TABLE dbjudicialdata.judicial_office_authorisation
ALTER COLUMN judicial_office_auth_id SET DEFAULT nextval('dbjudicialdata.judicial_office_auth_id_sequence');

--Create sequence for dbjudicialdata.judicial_office_appointment
CREATE SEQUENCE dbjudicialdata.judicial_office_appointment_id_sequence AS integer START 1 OWNED
BY dbjudicialdata.judicial_office_appointment.judicial_office_appointment_id;


ALTER TABLE dbjudicialdata.judicial_office_appointment
ALTER COLUMN judicial_office_appointment_id SET DEFAULT nextval('dbjudicialdata.judicial_office_appointment_id_sequence');

insert into dbjudicialdata.region_type(region_id,region_desc_en,region_desc_cy)
values ('0', 'default', 'default');


CREATE SEQUENCE dbjudicialdata.elink_audit_scheduler_id_sequence AS integer START 1 OWNED
BY dbjudicialdata.dataload_schedular_audit.id;


ALTER TABLE dbjudicialdata.dataload_schedular_audit

ALTER COLUMN id SET DEFAULT nextval('dbjudicialdata.elink_audit_scheduler_id_sequence');


CREATE SEQUENCE dbjudicialdata.elink_exception_records_id_sequence AS integer START 1 OWNED
BY dbjudicialdata.dataload_exception_records.id;


ALTER TABLE dbjudicialdata.dataload_exception_records
ALTER COLUMN id SET DEFAULT nextval('dbjudicialdata.elink_exception_records_id_sequence');