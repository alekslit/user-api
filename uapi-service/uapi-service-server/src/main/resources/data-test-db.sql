-- заполним таблицу roles:
-- Идентификаторы ролей (1 - USER, 2 - ADMIN)
insert into roles (name)
values('USER');
insert into roles (name)
values('ADMIN');
insert into roles (name)
values('DELETE');

-- тестовый Admin пользователь:
-- id 1:
insert into users (first_name, email, password, registration_date)
values ('admin1', 'admin1@test.ru', 'admin1', '2024-10-01T15:28:03.136329500');
insert into user_roles (user_id, role_id)
values ('1', '1'), ('1', '2');