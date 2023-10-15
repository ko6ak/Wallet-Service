SET search_path TO wallet_schema;

TRUNCATE player_actions CASCADE ;
TRUNCATE player CASCADE ;
TRUNCATE transaction;
TRUNCATE money_account CASCADE ;

ALTER SEQUENCE player_seq RESTART;
ALTER SEQUENCE transaction_seq RESTART;
ALTER SEQUENCE player_actions_seq RESTART;
ALTER SEQUENCE money_account_seq RESTART WITH 1001;

INSERT INTO money_account(balance) VALUES (300.01);

INSERT INTO player(name, login, password, money_account_id) VALUES ('Иван', 'ivan@gmail.com', '12345', 1001);

INSERT INTO player_actions(player_id, date_time, message)
VALUES (1, '2023-10-13 09:10:10', 'Успешная регистрация'),
       (1, '2023-10-13 09:11:10', 'Успешный вход'),
       (1, '2023-10-13 09:12:10', 'Создана транзакция с типом операции CREDIT, суммой 1000.00 и комментарием ''transaction #1'''),
       (1, '2023-10-13 09:13:10', 'Создана транзакция с типом операции DEBIT, суммой 699.99 и комментарием ''transaction #2'''),
       (1, '2023-10-13 09:14:10', 'Транзакция с типом операции CREDIT, суммой 1000.00 и комментарием ''transaction #1'' успешно выполнена'),
       (1, '2023-10-13 09:14:20', 'Транзакция с типом операции DEBIT, суммой 699.99 и комментарием ''transaction #2'' успешно выполнена'),
       (1, '2023-10-13 09:15:10', 'Успешный выход');

INSERT INTO transaction(date_time, description, operation, amount, money_account_id, is_processed)
VALUES ('2023-10-13 09:14:10', 'transaction #1', 'CREDIT', 1000.00, 1001, true),
       ('2023-10-13 09:14:20', 'transaction #2', 'DEBIT', 699.99, 1001, true);