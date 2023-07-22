INSERT INTO Users(username, password, first_name, last_name, cash_balance, portfolio_balance)
VALUES ("tmakaj213", "password", "Anthony", "Makaj", "1000", "2000")

UPDATE Stocks
SET quantity = 'q'
WHERE user_id = '1' AND stock_ticker = 's_t'
