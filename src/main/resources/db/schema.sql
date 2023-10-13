DROP TABLE IF EXISTS player_actions;
DROP TABLE IF EXISTS player;
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS money_account;

DROP SEQUENCE IF EXISTS player_seq;
DROP SEQUENCE IF EXISTS transaction_seq;
DROP SEQUENCE IF EXISTS player_actions_seq;
DROP SEQUENCE IF EXISTS money_account_seq;

CREATE SEQUENCE player_seq;
CREATE SEQUENCE transaction_seq;
CREATE SEQUENCE player_actions_seq;
CREATE SEQUENCE money_account_seq START WITH 1001;

CREATE TABLE money_account (
    id                  BIGINT      PRIMARY KEY DEFAULT nextval('money_account_seq'),
    balance             NUMERIC     NOT NULL
);

CREATE TABLE player (
    id                  BIGINT      PRIMARY KEY DEFAULT nextval('player_seq'),
    name                VARCHAR     NOT NULL,
    login               VARCHAR     NOT NULL,
    password            VARCHAR     NOT NULL,
    money_account_id    BIGINT      NOT NULL,
    FOREIGN KEY (money_account_id) REFERENCES money_account (id)
);
CREATE UNIQUE INDEX unique_login_index ON player (login);

CREATE TABLE player_actions (
    id                  BIGINT      PRIMARY KEY DEFAULT nextval('player_actions_seq'),
    player_id           BIGINT      NOT NULL,
    date_time           TIMESTAMP   NOT NULL,
    message             VARCHAR     NOT NULL,
    FOREIGN KEY (player_id) REFERENCES player (id)
);

CREATE TABLE transaction (
    id                  BIGINT      PRIMARY KEY DEFAULT nextval('transaction_seq'),
    date_time           TIMESTAMP   NOT NULL,
    description         VARCHAR     NOT NULL,
    operation           VARCHAR     NOT NULL,
    amount              NUMERIC     NOT NULL,
    money_account_id    BIGINT      NOT NULL,
    is_processed        BOOLEAN     NOT NULL,
    FOREIGN KEY (money_account_id) REFERENCES money_account (id)
);