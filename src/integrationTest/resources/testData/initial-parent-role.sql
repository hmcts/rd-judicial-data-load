insert into judicial_role_type (role_id, role_desc_en,role_desc_cy)
values ('1', 'role_desc_en', 'role_desc_cy') on conflict (role_id) do nothing;