ALTER TABLE users
    ADD profile_image_path VARCHAR(255);

ALTER TABLE books
    ADD image_url VARCHAR(255);

ALTER TABLE users
DROP
COLUMN college_reg_no;