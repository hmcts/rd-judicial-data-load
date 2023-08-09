-- Insert table script : dbjudicialdata.cft_region_type

insert into dbjudicialdata.cft_region_type(cft_region_id,cft_region_desc_en,cft_region_desc_cy) values ('0', 'default', 'default') on conflict (cft_region_id) do nothing;
insert into dbjudicialdata.cft_region_type(cft_region_id,cft_region_desc_en,cft_region_desc_cy) values ('1', 'London', '');
insert into dbjudicialdata.cft_region_type(cft_region_id,cft_region_desc_en,cft_region_desc_cy) values ('2', 'Midlands', '');
insert into dbjudicialdata.cft_region_type(cft_region_id,cft_region_desc_en,cft_region_desc_cy) values ('3', 'North East', '');
insert into dbjudicialdata.cft_region_type(cft_region_id,cft_region_desc_en,cft_region_desc_cy) values ('4', 'North West', '');
insert into dbjudicialdata.cft_region_type(cft_region_id,cft_region_desc_en,cft_region_desc_cy) values ('5', 'South East', '');
insert into dbjudicialdata.cft_region_type(cft_region_id,cft_region_desc_en,cft_region_desc_cy) values ('6', 'South West', '');
insert into dbjudicialdata.cft_region_type(cft_region_id,cft_region_desc_en,cft_region_desc_cy) values ('7', 'Wales', '');
insert into dbjudicialdata.cft_region_type(cft_region_id,cft_region_desc_en,cft_region_desc_cy) values ('10', 'Northern Ireland', '');
insert into dbjudicialdata.cft_region_type(cft_region_id,cft_region_desc_en,cft_region_desc_cy) values ('11', 'Scotland', '');
insert into dbjudicialdata.cft_region_type(cft_region_id,cft_region_desc_en,cft_region_desc_cy) values ('12', 'National', '');

