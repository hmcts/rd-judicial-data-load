
CREATE SEQUENCE dbjudicialdata.elink_dataload_schedular_job_id_sequence AS integer START 1 OWNED
BY dbjudicialdata.dataload_schedular_job.id;


ALTER TABLE dbjudicialdata.dataload_schedular_job
ALTER COLUMN id SET DEFAULT nextval('dbjudicialdata.elink_dataload_schedular_job_id_sequence');