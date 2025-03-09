CREATE TABLE IF NOT EXISTS Customer (
  id INT GENERATED ALWAYS AS IDENTITY,
  name VARCHAR(70) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Account (
  id INT GENERATED ALWAYS AS IDENTITY,
  customer_id INT references Customer(id) NOT NULL,
  balance DECIMAL(38,2) NOT NULL,
  currency VARCHAR(5) NOT NULL,
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS Transfer (
  id INT GENERATED ALWAYS AS IDENTITY,
  from_customer_id INT references Customer(id) NOT NULL,
  from_account_id INT references Account(id) NOT NULL,
  to_customer_id INT references Customer(id) NOT NULL,
  to_account_id INT references Account(id) NOT NULL,
  amount DECIMAL(38,2) NOT NULL,
  PRIMARY KEY (id)
);