CREATE SCHEMA IF NOT EXISTS wallet;
SET search_path TO wallet;

CREATE SEQUENCE player_seq;
CREATE SEQUENCE player_actions_seq;
CREATE SEQUENCE money_account_actions_seq;
CREATE SEQUENCE money_account_seq START WITH 1001;

CREATE TABLE money_account (
    id                  BIGINT      PRIMARY KEY DEFAULT nextval('money_account_seq'),
    balance             NUMERIC     NOT NULL
);

CREATE TABLE player (
    id                  BIGINT      PRIMARY KEY DEFAULT nextval('player_seq'),
    name                VARCHAR     NOT NULL,
    email               VARCHAR     NOT NULL,
    password            VARCHAR     NOT NULL,
    money_account_id    BIGINT      NOT NULL,
    FOREIGN KEY (money_account_id) REFERENCES money_account (id)
);
CREATE UNIQUE INDEX unique_login_index ON player (login);

CREATE TABLE money_account_actions (
    id                  BIGINT      PRIMARY KEY DEFAULT nextval('money_account_actions_seq'),
    money_account_id    BIGINT      NOT NULL,
    date_time           TIMESTAMP   NOT NULL,
    message             VARCHAR     NOT NULL,
    FOREIGN KEY (money_account_id) REFERENCES money_account (id)
);

CREATE TABLE player_actions (
    id                  BIGINT      PRIMARY KEY DEFAULT nextval('player_actions_seq'),
    player_id           BIGINT      NOT NULL,
    date_time           TIMESTAMP   NOT NULL,
    message             VARCHAR     NOT NULL,
    FOREIGN KEY (player_id) REFERENCES player (id)
);

CREATE TABLE transaction (
    id                  VARCHAR     NOT NULL PRIMARY KEY,
    date_time           TIMESTAMP   NOT NULL,
    description         VARCHAR     NOT NULL,
    operation           VARCHAR     NOT NULL,
    amount              NUMERIC     NOT NULL,
    money_account_id    BIGINT      NOT NULL,
    is_processed        BOOLEAN     NOT NULL,
    FOREIGN KEY (money_account_id) REFERENCES money_account (id)
);

INSERT INTO money_account(balance) VALUES (300.01);

INSERT INTO player(name, email, password, money_account_id) VALUES ('Иван', 'ivan@gmail.com', '12345', 1001);

INSERT INTO player_actions(player_id, date_time, message)
VALUES (1, '2023-10-13 09:10:10', 'Успешная регистрация'),
       (1, '2023-10-13 09:11:10', 'Успешный вход'),
       (1, '2023-10-13 09:12:10', 'Создана транзакция с типом операции CREDIT, суммой 1000.00 и комментарием ''transaction #1'''),
       (1, '2023-10-13 09:13:10', 'Создана транзакция с типом операции DEBIT, суммой 699.99 и комментарием ''transaction #2'''),
       (1, '2023-10-13 09:15:10', 'Успешный выход');

INSERT INTO money_account_actions(money_account_id, date_time, message)
VALUES (1001, '2023-10-13 09:14:10', 'Транзакция с типом операции CREDIT, суммой 1000.00 и комментарием ''transaction #1'' успешно выполнена'),
       (1001, '2023-10-13 09:14:20', 'Транзакция с типом операции DEBIT, суммой 699.99 и комментарием ''transaction #2'' успешно выполнена');

INSERT INTO transaction(id, date_time, description, operation, amount, money_account_id, is_processed)
VALUES ('694bc4c1-2987-4bd3-a71b-5809ef6686c1', '2023-10-13 09:14:10', 'transaction #1', 'CREDIT', 1000.00, 1001, true),
       ('6d4c78d7-b8b0-4e3a-a488-46508fe91e20', '2023-10-13 09:14:20', 'transaction #2', 'DEBIT', 699.99, 1001, true);