CREATE SEQUENCE dbjudicialdata.judicial_role_type_id_sequence AS integer START 1 OWNED
BY dbjudicialdata.judicial_role_type.role_id;


ALTER TABLE dbjudicialdata.judicial_role_type
ALTER COLUMN role_id SET DEFAULT nextval('dbjudicialdata.judicial_role_type_id_sequence');