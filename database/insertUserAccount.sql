INSERT INTO USER_ACCOUNT
(username, password, role, customer_id)
VALUES
('admin', 'admin123', 'ADMIN', NULL);

INSERT INTO USER_ACCOUNT
(username, password, role, customer_id)
VALUES
('vishnu', '1234', 'CUSTOMER', 1);

COMMIT;