-- update row script : dbjudicialdata.jrd_lrd_region_mapping

INSERT INTO dbjudicialdata.judicial_location_mapping (epimms_id,judicial_base_location_id,building_location_name,base_location_name,service_code) VALUES
	 ('1123', '1815', '', 'Employment Tribunal England and Wales', 'BHA1'),
	 ('1126', '768', '', 'Employment Tribunal Scotland', 'BFA1');
COMMIT;