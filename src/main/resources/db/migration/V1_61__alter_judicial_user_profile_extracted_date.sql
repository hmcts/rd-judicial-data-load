ALTER TABLE dbjuddata.judicial_user_profile ALTER COLUMN extracted_date TYPE date USING extracted_date::date;
ALTER TABLE dbjuddata.judicial_office_appointment ALTER COLUMN extracted_date TYPE date USING extracted_date::date;
