-- liquibase formatted sql

-- changeSet sli:5
INSERT INTO users(id,
                  first_name,
                  last_name,
                  phone,
                  username,
                  password,
                  role,
                  enabled)
VALUES (8,
        'Администратор',
        'Интернет-магазина',
        '+7(812)345-77-88',
        'admin@mail.ru',
        'password',
        'ADMIN',
        true)