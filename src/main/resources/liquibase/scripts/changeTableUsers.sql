-- liquibase formatted sql

-- changeset sLi:1
ALTER TABLE users RENAME COLUMN email TO username;
ALTER TABLE users ADD COLUMN enabled BOOLEAN;
ALTER TABLE users ADD PRIMARY KEY (username);
ALTER TABLE users ALTER COLUMN id TYPE BIGSERIAL;

-- changeset sli:2
CREATE TABLE IF NOT EXIST authorities
(
    id BIGSERIAL,
    username VARCHAR REFERENCES users(username),
    autority VARCHAR
);

