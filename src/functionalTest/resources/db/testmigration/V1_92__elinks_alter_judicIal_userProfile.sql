ALTER TABLE dbjudicialdata.judicial_user_profile ALTER COLUMN title TYPE VARCHAR(256);

ALTER TABLE dbjudicialdata.judicial_user_profile ALTER COLUMN initials TYPE VARCHAR(256);

ALTER TABLE dbjudicialdata.judicial_user_profile ALTER COLUMN known_as TYPE VARCHAR(256);

COMMIT;