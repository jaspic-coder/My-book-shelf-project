ALTER TABLE books
    ADD available BOOLEAN;

ALTER TABLE books
    ADD book_url VARCHAR(255);

ALTER TABLE books
    ADD cover_url VARCHAR(255);

ALTER TABLE books
    ADD rating INTEGER;

ALTER TABLE books
DROP
COLUMN availability_status;

ALTER TABLE books
DROP
COLUMN image_url;