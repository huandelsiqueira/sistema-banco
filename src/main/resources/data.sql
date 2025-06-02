INSERT INTO customers (id, name, email, birthday) VALUES (1, 'João Silva', 'joao@email.com', '1985-05-15');
INSERT INTO customers (id, name, email, birthday) VALUES (2, 'Maria Oliveira', 'maria@email.com', '1990-10-20');

INSERT INTO accounts (id, customer_id, account_type, balance) VALUES (100001, 1, 'CHECKING', 1500.00);
INSERT INTO accounts (id, customer_id, account_type, balance) VALUES (100002, 1, 'SAVINGS', 3000.00);
INSERT INTO accounts (id, customer_id, account_type, balance) VALUES (100003, 2, 'CHECKING', 2500.00);
