
CREATE SEQUENCE dbjudicialdata.elinks_responses_sequence AS integer START 1 OWNED
BY dbjudicialdata.elinks_responses.id;


ALTER TABLE dbjudicialdata.elinks_responses
ALTER COLUMN id SET DEFAULT nextval('dbjudicialdata.elinks_responses_sequence');