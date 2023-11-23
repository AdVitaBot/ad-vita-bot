UPDATE "localization"
SET "message"='Выберете пункт меню'
WHERE "code" = 'welcome_text';

UPDATE "localization"
SET "message"='Выберете сумму пожертвования или введите свою сумму, нажав на кнопку «Другая сумма»'
WHERE "code" = 'choose_amount_text';

UPDATE "theme"
SET "description" = 'Печенька с предсказаниями'
WHERE "id" = 1;

UPDATE "theme"
SET "description" = 'Моё внутреннее я'
WHERE "id" = 2;

INSERT INTO "localization" ("code", "message")
VALUES ('pay_button_text', 'Поддержать')
ON CONFLICT DO NOTHING;
