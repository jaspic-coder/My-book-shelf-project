ALTER TABLE payment
    DROP CONSTRAINT fk_payment_on_book;

ALTER TABLE payment
    DROP CONSTRAINT fk_payment_on_user;

ALTER TABLE reviews
    DROP CONSTRAINT fk_reviews_on_book;

ALTER TABLE reviews
    DROP CONSTRAINT fk_reviews_on_user;

ALTER TABLE user_password_reset_tokens
    DROP CONSTRAINT fkinxw48vu6y9ovjiqhbtldx40l;

DROP TABLE payment CASCADE;

DROP TABLE reviews CASCADE;

ALTER TABLE user_password_reset_tokens
    DROP COLUMN password_reset_tokens;

ALTER TABLE user_password_reset_tokens
    DROP COLUMN user_id;