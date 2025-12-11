--Enable Foreign Keys
PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS user (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    username TEXT NOT NULL UNIQUE,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    `role` TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS payment_method (
    payment_method_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    `provider` TEXT NOT NULL,
    last4 CHAR(4) NOT NULL, 
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS orders (
    order_id INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id INTEGER NOT NULL,
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    `status` TEXT NOT NULL,
    total_amount DECIMAL(10,2),

    FOREIGN KEY (user_id) REFERENCES user(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS payment (
    payment_id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL UNIQUE,
    payment_method_id INTEGER NOT NULL,
    amount REAL NOT NULL,
    shipping_fee REAL,
    `status` TEXT NOT NULL,
    paid_at DATETIME DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (payment_method_id) REFERENCES payment_method(payment_method_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS art_piece (
    product_id INTEGER PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    `description` TEXT,
    artist_name TEXT,
    price DECIMAL(10,2) NOT NULL,
    image_url TEXT NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT 1,  

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS order_item (
    order_item_id INTEGER PRIMARY KEY AUTOINCREMENT,
    order_id INTEGER NOT NULL,
    product_id INTEGER NOT NULL,
    quantity INTEGER,

    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES art_piece(product_id) ON DELETE CASCADE
);


INSERT OR IGNORE INTO user (username, first_name, last_name, email, password_hash, role)
VALUES (
    'admin',
    'System',
    'Administrator',
    'admin@example.com',
    'admin',
    'ADMIN'
);