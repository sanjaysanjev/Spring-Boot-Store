


CREATE TABLE addresses
(
    id      BIGINT NOT NULL AUTO_INCREMENT ,
    street  VARCHAR(255) NOT NULL,
    city    VARCHAR(255) NOT NULL,
    state   VARCHAR(255) NOT NULL,
    zip     VARCHAR(255) NOT NULL,
    user_id BIGINT       NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE categories
(
    id   TINYINT NOT NULL AUTO_INCREMENT ,
    name VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE products
(
    id            BIGINT NOT NULL AUTO_INCREMENT ,
    name          VARCHAR(255)   NOT NULL,
    price         DECIMAL(10, 2) NOT NULL,
    `description` LONGTEXT       NOT NULL,
    category_id   TINYINT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE profiles
(
    id             BIGINT NOT NULL,
    bio            LONGTEXT NULL,
    phone_number   VARCHAR(15) NULL,
    date_of_birth  date NULL,
    loyalty_points INT UNSIGNED DEFAULT 0,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE users
(
    id       BIGINT NOT NULL AUTO_INCREMENT ,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (id)
);

CREATE TABLE wishlist
(
    product_id BIGINT NOT NULL,
    user_id    BIGINT NOT NULL,
    CONSTRAINT `PRIMARY` PRIMARY KEY (product_id, user_id)
);

ALTER TABLE addresses
    ADD FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE addresses
    ADD INDEX addresses_users_id_fk (user_id);

ALTER TABLE products
    ADD FOREIGN KEY (category_id) REFERENCES categories (id);


ALTER TABLE products
    ADD INDEX fk_category (category_id);


ALTER TABLE wishlist
    ADD CONSTRAINT fk_wishlist_on_product FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE;

ALTER TABLE wishlist
    ADD FOREIGN KEY (user_id) REFERENCES users (id);


ALTER TABLE wishlist
    ADD INDEX fk_wishlist_on_user (user_id);


ALTER TABLE profiles
    ADD  FOREIGN KEY (id) REFERENCES users (id) ;