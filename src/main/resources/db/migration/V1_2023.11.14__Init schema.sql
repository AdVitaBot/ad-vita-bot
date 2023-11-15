CREATE TABLE IF NOT EXISTS "theme"
(
    "id"                  SERIAL PRIMARY KEY,
    "min_donation_amount" NUMERIC(10, 2) NOT NULL,
    "max_donation_amount" NUMERIC(10, 2) NOT NULL,
    "description"         VARCHAR(255)   NOT NULL
);

COMMENT ON COLUMN "theme".id IS 'Theme unique id';
COMMENT ON COLUMN "theme".min_donation_amount IS 'Minimum donation amount on this theme';
COMMENT ON COLUMN "theme".max_donation_amount IS 'Maximum donation amount on this theme';
COMMENT ON COLUMN "theme".description IS 'Theme user friendly description';

CREATE TABLE IF NOT EXISTS "participant"
(
    "chat_id"  INTEGER     NOT NULL UNIQUE,
    "state"    VARCHAR(32) NOT NULL,
    "theme_id" INTEGER,
    "amount"   NUMERIC(10, 2)
);

COMMENT ON COLUMN "participant".chat_id IS 'Participant chat identifier';
COMMENT ON COLUMN "participant".state IS 'Participant state in bot';
COMMENT ON COLUMN "participant".theme_id IS 'Participant currently chosen theme';
COMMENT ON COLUMN "participant".amount IS 'Participant amount to donate';

CREATE TABLE IF NOT EXISTS "drawing"
(
    "id"       SERIAL PRIMARY KEY,
    "theme_id" INTEGER      NOT NULL,
    "caption"  VARCHAR(255) NOT NULL,
    "image"    bytea        NOT NULL
);

COMMENT ON COLUMN "drawing".id IS 'Drawing unique id';
COMMENT ON COLUMN "drawing".theme_id IS 'Foreign key on theme';
COMMENT ON COLUMN "drawing".caption IS 'User friendly image caption';
COMMENT ON COLUMN "drawing".image IS 'Image content';

CREATE INDEX IF NOT EXISTS "idx_drawing__theme_id" ON "drawing" ("theme_id");

ALTER TABLE "drawing"
    DROP CONSTRAINT IF EXISTS "drawing_theme",
    ADD CONSTRAINT "drawing_theme" FOREIGN KEY ("theme_id") REFERENCES "theme" ("id") ON DELETE CASCADE;

CREATE TABLE IF NOT EXISTS "donation"
(
    "id"             SERIAL PRIMARY KEY,
    "status"         VARCHAR(64)    NOT NULL,
    "donator_name"   VARCHAR(512),
    "donator_email"  VARCHAR(512),
    "participant_id" INTEGER        NOT NULL,
    "drawing_id"     INTEGER        NOT NULL,
    "amount"         NUMERIC(10, 2) NOT NULL,
    "donation_date"  TIMESTAMP      NOT NULL
);

COMMENT ON COLUMN "donation".id IS 'Donation unique id';
COMMENT ON COLUMN "donation".status IS 'Donation status: CREATED, RECEIVED, CANCELED';
COMMENT ON COLUMN "donation".donator_name IS 'Donation person name';
COMMENT ON COLUMN "donation".donator_email IS 'Donation email';
COMMENT ON COLUMN "donation".participant_id IS 'Foreign key on participant';
COMMENT ON COLUMN "donation".drawing_id IS 'Foreign key on drawing';
COMMENT ON COLUMN "donation".amount IS 'Donation amount';
COMMENT ON COLUMN "donation".donation_date IS 'Donation date';

CREATE INDEX IF NOT EXISTS "idx_donation__participant_id_drawing_id_status" ON "donation" ("participant_id", "drawing_id", "status");

ALTER TABLE "donation"
    DROP CONSTRAINT IF EXISTS "donation_participant",
    ADD CONSTRAINT "donation_participant" FOREIGN KEY ("participant_id") REFERENCES "participant" ("chat_id"),
    DROP CONSTRAINT IF EXISTS "donation_drawing",
    ADD CONSTRAINT "donation_drawing" FOREIGN KEY ("drawing_id") REFERENCES "drawing" ("id");

CREATE TABLE IF NOT EXISTS "localization"
(
    "code"    VARCHAR(64) NOT NULL UNIQUE,
    "message" TEXT        NOT NULL
);

COMMENT ON COLUMN "localization".code IS 'Localization unique code';
COMMENT ON COLUMN "localization".message IS 'Localization message';

CREATE TABLE IF NOT EXISTS "bot_parameters"
(
    "code"  VARCHAR(64)   NOT NULL UNIQUE,
    "name"  VARCHAR(64)   NOT NULL,
    "type"  VARCHAR(32)   NOT NULL,
    "value" VARCHAR(1024) NOT NULL
);

COMMENT ON COLUMN "bot_parameters".code IS 'Bot parameter unique code';
COMMENT ON COLUMN "bot_parameters".name IS 'Bot parameter user friendly name';
COMMENT ON COLUMN "bot_parameters".type IS 'Bot parameter type: STRING, DATE';
COMMENT ON COLUMN "bot_parameters".value IS 'Bot parameter value';
