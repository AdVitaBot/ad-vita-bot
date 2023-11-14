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
VALUES ('welcome_text', 'Благодарим вас за интерес к акции. Скорее выбирайте пункт меню'),
       ('pick_one_theme_text', 'Выберите одну из тем'),
       ('pick_exists_theme_text', 'Не знаю такую тему,\nВыберите одну из *существующих* тем'),
       ('choose_amount_text',
        'Пожалуйста, выберите одну из предложенных сумм пожертвования или укажите свою. Нажмите на кнопку «Другая сумма», чтобы ввести произвольную сумму.'),
       ('input_amount_text',
        'Пожалуйста отправьте в сообщении сумму от 100 ₽ до 900 000 ₽, которую вы готовы пожертвовать'),
       ('input_amount_error_text', 'Пожалуйста укажите корректную сумму пожертвования'),
       ('try_more_button_text', 'Ещё картинку'),
       ('thankfully_message_text',
        'Мы вместе с Татьяной Задорожней желаем хорошего дня и благодарим за помощь подопечным фонда AdVita!'),
       ('theme_1_name', 'Печеньки с предсказаниями'),
       ('theme_2_name', 'Твое внутреннее я'),
       ('theme_1_message_text', 'Ловите вашу печеньку с предсказанием'),
       ('theme_2_message_text', 'Ловите ваше внутренне я'),
       ('other_amount_text', 'Другая сумма'),
       ('about_fund_text', 'Подробнее о фонде'),
       ('fund_url', 'https://advita.ru'),

       ('invoice_title', 'Пожертвование'),
       ('invoice_description', 'Пожертвование в фонд AdVita'),
       ('invoice_price_label', 'Пожертвование');

/**
  Fill themes
 */
INSERT INTO "theme" ("id", "min_donation_amount", "max_donation_amount", "description")
VALUES (1, 100.00, 900000.00, 'Печеньки с предсказаниями'),
       (2, 100.00, 900000.00, 'Твое внутреннее я');