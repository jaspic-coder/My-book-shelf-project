ALTER TABLE users
    ADD bio VARCHAR(500);

ALTER TABLE users
    ADD username VARCHAR(255);

ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

ALTER TABLE users
DROP
COLUMN name;