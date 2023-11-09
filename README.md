## Wallet-Service

Однопользовательское REST приложение, которое управляет кредитными/дебетовыми транзакциями от имени залогиненного игрока. 
Сохранения происходят в БД (PostgreSQL в Docker). 

### Endpoints

URL приложения: `http://localhost:8080`

`/login` - вход в приложение с помощью email и пароля. Возвращает информацию об Игроке и токен аутентификации.<br />
`/logout` - выход из приложения. Требуется, выданный при логине, токен.<br />
`/player-register` - регистрация нового Игрока. Возвращает информацию о зарегистрированном Игроке.<br />
`/balance` - возвращает баланс счета Игрока. Требуется, выданный при логине, токен.<br />
`/transaction-log` - возвращает лог выполненных транзакций счета Игрока. Требуется, выданный при логине, токен.<br />
`/full-log` - возвращает полный лог действий Игрока включая информацию о транзакциях.<br />
`/transaction-register` - создание транзакции от имени залогиненного Игрока. Требуется, выданный при логине, токен.<br />
`/process` - запуск обработки транзакций.<br />

### Работа с приложением

В приложение есть тестовый Игрок.<br />
`/login` post-запрос `{ "email":"ivan@gmail.com", "password":"12345" }`<br />
`/logout` post-запрос, например: `{ "token": "eyJhbGciOiJIUzI1NiJ9..."}`<br />
`/player-register` post-запрос `{ "name":"Petr", "email":"petr@ya.ru", "password":"54321" }`<br />
`/balance` post-запрос, например: `{ "token": "eyJhbGciOiJIUzI1NiJ9..."}`<br />
`/transaction-log` post-запрос, например: `{ "token": "eyJhbGciOiJIUzI1NiJ9..."}`<br />
`/full-log` post-запрос `{ "playerId": 1 }`<br />
`/transaction-register` post-запрос `{ "id":"711c12eb-9f98-417d-af8f-57f902d30008", "operationType":"CREDIT", "amount":"100.00", "description":"incoming", "token": "eyJhbGciOiJIUzI1NiJ9..." }`<br />
`/process` get-запрос<br />

### Порядок запуска из консоли

1. Склонировать репозиторий: `git clone https://github.com/ko6ak/Wallet-Service.git`. 
2. Перейти в папку с программой: `cd Wallet-Service`
3. Запустить docker-образы (файл docker.yml находится в корне проекта): `docker compose -f docker.yml up`
4. Собрать приложение: `mvn clean install`
5. Отправить JSON-запрос через Postman.


Отметка для себя:
Для того, чтобы работали аспекты нужно сделать clean, а затем сразу запустить проект(то есть не надо выполнять install).