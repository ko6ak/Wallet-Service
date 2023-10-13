## Wallet-Service

Консольное приложение, которое управляет кредитными/дебетовыми транзакциями от имени игроков. 
Сохранения происходят во временную память, то есть после перезапуска приложения хранилище обнуляется.

### Меню программы
```
Wallet-Service. Введите номер пункта меню
(1) Регистрация Игрока
(2) Вход
(3) Вывести баланс
(4) Вывести лог завершенных транзакций
(5) Создать транзакцию
(6) Выход
(7) Запустить обработку транзакций
(8) Вывести лог действий пользователя
(9) Выход из программы
```

### Как это работает

`(1)` Регистрирует Игрока, требуя ввести имя, логин и пароль.<br />
`(2)` Позволяет войти в систему с помощью логина и пароля. Если логин или пароль не правильные в консоль, будет выведена ошибка.<br />
`(3)` Выводит баланс вошедшего в систему Игрока.<br />
`(4)` Выводит лог завершенных транзакций вошедшего в систему Игрока.<br />
`(5)` Создает и регистрирует в системе транзакцию для счета от имени вошедшего в систему Игрока, требуя ввести тип операции (списание/пополнение), сумму и комментарий. Транзакции автоматически присваивается уникальный id.<br />
`(6)` Выход из системы для вошедшего в систему Игрока.<br />
`(7)` Запуск выполнения зарегистрированных транзакций. Вход Игрока в систему не требуется. Если сумма списания больше баланса на счете Игрока, будет выведена ошибка в консоль.<br />
`(8)` Выводит в консоль лод действий игрока, включая транзакции. Вход Игрока в систему не требуется.<br />
`(9)` Завершает работу программы.

### Порядок запуска из консоли

1. Склонировать репозиторий: `git clone https://github.com/ko6ak/Wallet-Service.git`. 
2. Перейти в папку с программой: `cd Wallet-Service`
3. Собрать приложение: `mvn clean install`
4. Запустить приложение: `mvn exec:java -Dexec.mainClass="org.wallet_service.in.App"`