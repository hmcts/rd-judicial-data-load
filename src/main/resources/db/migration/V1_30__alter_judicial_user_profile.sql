ALTER TABLE judicial_user_profile ADD COLUMN is_judge Boolean;
ALTER TABLE judicial_user_profile ADD COLUMN is_panel_member Boolean;
ALTER TABLE judicial_user_profile ADD COLUMN is_magistrate Boolean;
ALTER TABLE judicial_user_profile ADD COLUMN mrd_created_time Timestamp;
ALTER TABLE judicial_user_profile ADD COLUMN mrd_updated_time Timestamp;
ALTER TABLE judicial_user_profile ADD COLUMN mrd_deleted_time Timestamp;
COMMIT;
