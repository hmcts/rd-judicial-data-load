ALTER TABLE judicial_office_appointment drop CONSTRAINT elinks_Id_fk1;
ALTER TABLE judicial_office_authorisation drop CONSTRAINT elinks_Id;
ALTER TABLE judicial_user_profile DROP CONSTRAINT elinks_Id;
ALTER TABLE judicial_user_profile RENAME COLUMN elinks_Id TO per_Id;
ALTER TABLE judicial_user_profile ADD PRIMARY KEY (per_Id);

ALTER TABLE judicial_office_appointment RENAME COLUMN elinks_Id TO per_Id;
ALTER TABLE judicial_office_appointment ADD CONSTRAINT per_Id_fk FOREIGN KEY (per_Id)
REFERENCES judicial_user_profile (per_Id);

ALTER TABLE judicial_office_authorisation RENAME COLUMN elinks_Id TO per_Id;
ALTER TABLE judicial_office_authorisation ADD CONSTRAINT per_Id_fk FOREIGN KEY (per_Id)
REFERENCES judicial_user_profile (per_Id);