CREATE TABLE IF NOT EXISTS Users (
  user_id INTEGER PRIMARY KEY AUTOINCREMENT,
  username VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  cash_balance DECIMAL(10, 2) DEFAULT 0,
  portfolio_balance DECIMAL(10, 2) DEFAULT 0
);

CREATE TABLE IF NOT EXISTS Stocks (
	stock_id INTEGER PRIMARY KEY AUTOINCREMENT,
	user_id INT NOT NULL,
	stock_ticker VARCHAR(10) NOT NULL,
	quantity INT NOT NULL,
	
	FOREIGN KEY (user_id) REFERENCES Users(user_id)
);