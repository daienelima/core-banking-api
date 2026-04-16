CREATE TABLE accounts (
                          id BIGINT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL,
                          balance NUMERIC(19,2) NOT NULL
);

CREATE SEQUENCE transactions_seq START WITH 1 INCREMENT BY 1;
CREATE TABLE transactions (
                              id BIGINT PRIMARY KEY DEFAULT nextval('transactions_seq'),
                              from_account_id BIGINT NOT NULL,
                              to_account_id BIGINT NOT NULL,
                              amount NUMERIC(19,2) NOT NULL,
                              created_at TIMESTAMP NOT NULL
);

INSERT INTO accounts (id, name, balance) VALUES
                                             (1, 'Alice', 1000.00),
                                             (2, 'Bob', 500.00),
                                             (3, 'Charlie', 200.00);