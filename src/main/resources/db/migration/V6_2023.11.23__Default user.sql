INSERT INTO "user" ("login", "password")
VALUES ('admin', '$2a$12$Mdbj8Ie64kExm08Y.PZ1AejQ2tmAphaBXO1tmHSJalLuetQ8CCdHK')
ON CONFLICT DO NOTHING;

ALTER TABLE "user"
    ADD CONSTRAINT "user_login_lowercase_ck"
        CHECK ("login" = lower("login"));
