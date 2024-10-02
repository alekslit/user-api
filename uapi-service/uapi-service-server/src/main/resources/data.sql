-- заполним таблицу roles:
-- Идентификаторы ролей (1 - USER, 2 - ADMIN)
insert into roles (name)
values ('USER'), ('ADMIN'), ('DELETE')
ON CONFLICT (name) DO NOTHING;

-- тестовый Admin пользователь:
-- id 1:
insert into users (first_name, email, password, registration_date)
values ('admin1', 'admin1@test.ru', 'admin1', '2024-10-01T15:28:03.136329500')
ON CONFLICT (email) DO NOTHING;
insert into user_roles (user_id, role_id)
values ('1', '1'), ('1', '2')
ON CONFLICT (user_id, role_id) DO NOTHING;