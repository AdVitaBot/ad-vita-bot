/**
  Fill bot dynamic parameters
 */
INSERT INTO "bot_parameters" ("code", "name", "type", "value")
VALUES ('invoice_provider_token', 'Токен платёжной системы', 'STRING', 'Заполнить'),
       ('deactivation_date', 'Дата окончания акции', 'DATE', '3000-12-31');

/**
  Fill default localizations
 */
INSERT INTO "localization" ("code", "message")
VALUES ('welcome_text', 'Добро пожаловать в Благотворительный фонд помощи онкологическим больным AdVita («Ради жизни»)!'),
 ('pick_one_theme_text', 'Выберите одну из тем'),
 ('pick_exists_theme_text', 'Не знаю такую тему,\nВыберите одну из *существующих* тем');
