
ALTER TABLE dataload_schedular_audit  RENAME COLUMN scheduler_status TO status;

ALTER TABLE dataload_schedular_audit ADD COLUMN file_name varchar(128);

ALTER TABLE dataload_exception_records DROP COLUMN file_name;

