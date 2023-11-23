CREATE TABLE IF NOT EXISTS "user"
(
    "id"         SERIAL PRIMARY KEY,
    "login"      VARCHAR(128) UNIQUE NOT NULL,
    "password"   VARCHAR(128)        NOT NULL
);

COMMENT ON COLUMN "user".id IS 'User unique identifier';
COMMENT ON COLUMN "user".login IS 'User login';
COMMENT ON COLUMN "user".password IS 'User encrypted password';
