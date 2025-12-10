--Enable Foreign Keys
PRAGMA foreign_keys = ON;

--Enable foreign key support in SpringBoot with the following:
--spring.datasource.hikari.connection-init-sql=PRAGMA foreign_keys=ON

--Diffrences in SQLite syntax from Entity Relationship Diagram:
--Use AUTOINCREMENT instead of Identity
--Use TEXT instead of varchar


CREATE TABLE IF NOT EXISTS User (
    UserId INTEGER PRIMARY KEY AUTOINCREMENT,
    FirstName  TEXT NOT NULL,
    LastName TEXT NOT NULL,
    Email TEXT NOT NULL,
    `Password` TEXT NOT NULL,
    `Role` TEXT NOT NULL,
    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP
)

CREATE TABLE IF NOT EXISTS PaymentMethod (
    PaymentMethodId INTEGER PRIMARY KEY AUTOINCREMENT
    UserId INTEGER NOT NULL
    `Provider` TEXT NOT NULL
    Last4 CHAR NOT NULL

    --Foreign Keys
    FOREIGN KEY (UserId) REFERENCES User(UserId) ON DELETE CASCADE
)

CREATE TABLE IF NOT EXISTS Order (
    OrderId INTEGER PRIMARY KEY AUTOINCREMENT
    UserId INTEGER NOT NULL
    OrderDate DATETIME DEFAULT CURRENT_TIMESTAMP
    `Status` TEXT NOT NULL
    TotalAmount DECIMAL(10,2)

    --Foreign Keys
    FOREIGN KEY (UserId) REFERENCES User(UserId) ON DELETE CASCADE
)

CREATE TABLE IF NOT EXISTS Payment (
    PaymentId INTEGER PRIMARY KEY AUTOINCREMENT
    OrderId INTEGER NOT NULL
    PaymentMethodId INTEGER NOT NULL
    Amount DECIMAL(10,2) NOT NULL
    `Status` TEXT NOT NULL
    PaidAt DATETIME DEFAULT CURRENT_TIMESTAMP

    --Foreign Keys
    FOREIGN KEY (OrderId) REFERENCES Order(OrderId) ON DELETE CASCADE
    FOREIGN KEY (PaymentMethodId) REFERENCES PaymentMethod(PaymentMethodId) ON DELETE CASCADE
)

CREATE TABLE IF NOT EXISTS Product (
    ProductId INTEGER PRIMARY KEY AUTOINCREMENT
    Title TEXT NOT NULL
    `Description` TEXT NOT NULL
    Price DECIMAL(10,2) NOT NULL
    ImageUrl TEXT NOT NULL
    IsActive BIT NOT NULL
    CreatedAt DATETIME DEFAULT CURRENT_TIMESTAMP
)

CREATE TABLE IF NOT EXISTS OrderItem (
    id INTEGER PRIMARY KEY AUTOINCREMENT
    OrderId INTEGER NOT NULL
    ProductId INTEGER NOT NULL

    --Foreign Keys
    FOREIGN KEY (OrderId) REFERENCES Order(OrderId) ON DELETE CASCADE
    FOREIGN KEY (ProductId) REFERENCES Product(ProductId) ON DELETE CASCADE
)