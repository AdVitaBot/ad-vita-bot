[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=AdVitaBot_ad-vita-bot&metric=bugs)](https://sonarcloud.io/summary/new_code?id=AdVitaBot_ad-vita-bot)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=AdVitaBot_ad-vita-bot&metric=coverage)](https://sonarcloud.io/summary/new_code?id=AdVitaBot_ad-vita-bot)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=AdVitaBot_ad-vita-bot&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=AdVitaBot_ad-vita-bot)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=AdVitaBot_ad-vita-bot&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=AdVitaBot_ad-vita-bot)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=AdVitaBot_ad-vita-bot&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=AdVitaBot_ad-vita-bot)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=AdVitaBot_ad-vita-bot&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=AdVitaBot_ad-vita-bot)


# Ad Vita Bot
## Описание задачи
Благотворительный фонд AdVita («Ради жизни») с 2002 года помогает детям и взрослым лечиться от рака в клиниках Санкт-Петербурга. 
Для запуска благотворительной акции фонду требуется помощь по разработке чата-бота в мессенджере Telegram.

### Цель проекта
Проведение благотворительной онлайн-акции для привлечения денежных средств в фонд и привлечения внимания к проблематике, 
которую решает фонд.

### Описание чат-бота
Попадая в чат-бот участник акции нажимает кнопку «начать», после чего появляется сообщение, 
содержащее логотип фонда и приветствие. 

Далее участнику предлагается выбрать один из двух пунктов меню, 
то есть одну из двух тем (каждая из тем будет содержать разные рисунки). 

После чего участнику предлагается выбрать сумму пожертвования, 
внести адрес электронной почты для получения чека об оплате. 

В этом же чате открывается ссылка на оплату, участник переходит по ней, вносит данные банковской карты, 
производит оплату. 

Сразу после этого участник автоматически возвращается в чат, 
где ему приходит ряд сообщений: благодарность за пожертвование, обещанная картинка и предложение попробовать снова.

Нажимая «попробовать снова», участник возвращается на этап выбора темы, соответственно, 
может выбрать вторую из предложенных тем, а может выбрать снова первую тему, снова произвести оплату и получить картинку. 

Количество раз участия одного человека не ограниченно, при условии, что каждый раз он совершает пожертвование. Получить рисунок без пожертвования невозможно.

### Важные условия
1. В боте должна быть привязана платежная система.
2. Чат должен содержать возможность первоначального выбора из двух тем.
3. Каждая тема будет содержать по 30 рисунков. Очередность их предоставления участнику рандомна.


## Запуск бота

### Предусловия
1. Необходим предустановленный на сервер postgres, версии не ниже 14.0.
2. В postgres должна быть создана база данных `ad_vita_bot`. 
В случае использования другого наименования необходимо внести правки в конфигурацию.
3. Необходим предустановленный на сервер jdk, версии не ниже 21.

### Последовательность
1. Скачать последний релиз бота - jar файл с именем `ad_vita_bot-<version>.jar`: https://github.com/orgs/AdVitaBot/packages?repo_name=ad-vita-bot
2. В директории со скаченным ботом необходимо создать файл конфигурации
3. В директории со скаченным ботом создать файл запуска
4. Создать новый сервис для запуска бота как демона
5. Запустить бота `sudo systemctl start ad-vita-bot`
6. Проверить, в логах, что сервис запущен успешно: `journalctl -f -u ad-vita-bot` содержит строку: `Started Application`
7. Войти в административную часть и заполнить картинки и платёжный токен.

### Конфигурация
Название файла: `application-PROD.properties`

Содержимое
```properties
# Порт для запуска административной части
server.port=8083
# -------------------------- Data Source -----------------------------
# Путь до БД
spring.datasource.url=jdbc:postgresql://localhost:5433/ad_vita_bot
# Логин пользователя БД
spring.datasource.username=postgres
# Пароль пользователя БД
spring.datasource.password = a12345
# Используемая БД, не менять
spring.datasource.driver-class-name=org.postgresql.Driver
# Используемая схема в БД
spring.jpa.properties.hibernate.default_schema=ad_vita_bot
# -------------------------- Data Source -----------------------------
# -------------------------- Flyway Conf -----------------------------
# Включатель миграции скриптов БД, не менять
spring.flyway.enabled=true
# Используемая схема в БД
spring.flyway.schemas=ad_vita_bot
# -------------------------- Flyway Conf -----------------------------
# --------------------------  App Config -----------------------------
# Логин пользователя для доступа к административной части
app.bot.admin.username=admin
# Пароль пользователя для доступа к административной части
# Обязательно сгенерировать новый
app.bot.admin.password=password
# Токен бота от @BotFather
app.bot.token = a12345
# --------------------------  App Config -----------------------------
```

Необходимо изменить настройки для подключения к Postgres и настройки административной панели.

### Файл запуска
Файл с именем: `startup.sh`

Содержимое
```shell
#!/bin/sh
sudo /usr/bin/java -Dspring.profiles.active=PROD -Xms1G -Xmx4G -XX:+UseZGC -jar *.jar
```

Сделать файл запускаемым
```shell
chmod +x startup.sh
```

### Создание сервиса в linux

По пути: `/etc/systemd/system` необходимо создать файл `ad-vita-bot.service`.

#### Содержимое файла
```properties
[Unit]
Description=Ad Vita Bot
[Service]
User=root
# The configuration file application.properties should be here:
#change this to your workspace
WorkingDirectory=/root/ad_vita_bot/
#path to executable.
#executable is a bash script which calls jar file
ExecStart=/root/ad_vita_bot/startup.sh
SuccessExitStatus=143
TimeoutStopSec=10
Restart=on-failure
RestartSec=5
[Install]
WantedBy=multi-user.target
```

#### Поля которые нужно заполнить

| Поле             | Описание                         |
|------------------|----------------------------------|
| User             | Пользователь для запуска сервиса |
| WorkingDirectory | Директория с jar файлом          |
| ExecStart        | Путь до запускаемого файла       |

#### Регистрация изменений
```shell
sudo systemctl daemon-reload
```