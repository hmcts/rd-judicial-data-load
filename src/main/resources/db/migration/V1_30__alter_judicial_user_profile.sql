ALTER TABLE judicial_user_profile ADD COLUMN Is_Judge Boolean;
ALTER TABLE judicial_user_profile ADD COLUMN Is_Panel_Member Boolean;
ALTER TABLE judicial_user_profile ADD COLUMN Is_Magistrate Boolean;
ALTER TABLE judicial_user_profile ADD COLUMN MRD_Created_Time Timestamp;
ALTER TABLE judicial_user_profile ADD COLUMN MRD_Updated_Time Timestamp;
ALTER TABLE judicial_user_profile ADD COLUMN MRD_Deleted_Time Timestamp;
COMMIT;
