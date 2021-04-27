
CREATE TABLE dataload_schedular_job(
  id serial NOT NULL,
  publishing_status varchar(16),
  job_start_time timestamp NOT NULL,
  job_end_time timestamp,
  CONSTRAINT dataload_schedular_job_pk PRIMARY KEY (id)
)