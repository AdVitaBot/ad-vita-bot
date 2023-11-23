CREATE TABLE IF NOT EXISTS "update_queue"
(
    "id"                 INTEGER     NOT NULL PRIMARY KEY,
    "chat_id"            BIGINT      NOT NULL,
    "content"            text        NOT NULL,
    "status"             VARCHAR(16) NOT NULL,
    "status_description" varchar(512),
    "rewrite"            INTEGER     NOT NULL,
    "created_at"         timestamp   NOT NULL,
    "modified_at"        timestamp   NOT NULL
);

COMMENT ON COLUMN "update_queue".id IS 'Update unique identifier';
COMMENT ON COLUMN "update_queue".chat_id IS 'Update distant chat identifier';
COMMENT ON COLUMN "update_queue".content IS 'Update content';
COMMENT ON COLUMN "update_queue".status IS 'Update processing status';
COMMENT ON COLUMN "update_queue".status_description IS 'Update status description';
COMMENT ON COLUMN "update_queue".rewrite IS 'Update processing rewriting count';
COMMENT ON COLUMN "update_queue".created_at IS 'Update receiving date-time';
COMMENT ON COLUMN "update_queue".modified_at IS 'Last date-time of update status change';

CREATE INDEX IF NOT EXISTS "idx_update_queue__id_chat_id_created_at" ON "update_queue" ("status", "chat_id", "created_at");