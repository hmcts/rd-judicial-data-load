insert into region_type(region_id,region_desc_en,region_desc_cy)
values ('0', 'default', 'default') on conflict (region_id) do nothing;

insert into base_location_type(base_location_id,court_name,court_type,circuit,area_of_expertise)
values ('0', 'default','default', 'default','default')
on conflict (base_location_id) do nothing;

insert into judicial_role_type (role_id, role_desc_en,role_desc_cy)
values ('0', 'default', 'default') on conflict (role_id) do nothing;

commit;