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
       ('try_more_text', 'Не хотели бы вы попробовать ещё?'),
       ('try_more_button_text', 'Попробовать ещё'),
       ('thankfully_message_text',
        'Большое спасибо вам за помощь! Мы это очень ценим! Подробнее о нас https://advita.ru'),
       ('theme_1_name', 'Предсказание на день'),
       ('theme_2_name', 'Моё второе я'),
       ('theme_1_message_text', 'Ловите ваше предсказание на сегодня'),
       ('theme_2_message_text', 'Ловите, кто вы сегодня'),
       ('other_amount_text', 'Другая сумма'),

       ('invoice_title', 'Пожертвование'),
       ('invoice_description', 'Пожертвование в фонд AdVita'),
       ('invoice_price_label', 'Пожертвование');

/**
  Fill themes
 */
INSERT INTO "theme" ("id", "min_donation_amount", "max_donation_amount", "description")
VALUES (1, 100.00, 900000.00, 'Предсказание на день'),
       (2, 100.00, 900000.00, 'Моё второе я');