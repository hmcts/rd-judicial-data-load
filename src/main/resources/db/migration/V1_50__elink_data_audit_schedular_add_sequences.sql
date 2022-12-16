create sequence elink_audit_scheduler_id_seq;

ALTER TABLE dbjudicialdata.dataload_schedular_audit ALTER COLUMN id
SET DEFAULT nextval('elink_audit_scheduler_id_seq');
