ALTER TABLE "drawing"
ADD COLUMN IF NOT EXISTS "active" BOOL NOT NULL DEFAULT TRUE;

COMMENT ON COLUMN "drawing".active IS 'Is drawing actively used in bot';