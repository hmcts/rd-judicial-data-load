insert into judicial_role_type (role_id, role_desc_en,role_desc_cy) values ('1', 'role_desc_en', 'role_desc_cy');

/* Aggregation tables
CREATE TABLE aggregationRepo (
 id varchar(255) NOT NULL,
 exchange bytea NOT NULL,
 body varchar(1000)
 constraint aggregationRepo_pk PRIMARY KEY (id)
);

CREATE TABLE aggregationRepo_completed (
 id varchar(255) NOT NULL,
 exchange bytea NOT NULL,
 body varchar(1000),
 constraint aggregationRepo3_completed_pk PRIMARY KEY (id)
);
*/