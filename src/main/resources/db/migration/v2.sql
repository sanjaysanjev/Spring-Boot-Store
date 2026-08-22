CREATE TABLE carts (
    id BINARY(16) Not Null DEFAULT (UUID_TO_BIN(UUID())),
    Date_Created Date default (curdate()) Not Null,
    PRIMARY KEY (id)
);


SELECT BIN_TO_UUID(id) AS id, Date_Created
FROM carts;

CREATE TABLE Cart_Items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cart_id BINARY(16) NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT DEFAULT 1 NOT NULL,

    CONSTRAINT cart_items_cart_id_fk
        FOREIGN KEY (cart_id) REFERENCES carts(id) ON DELETE CASCADE,

    CONSTRAINT cart_items_product_id_fk
        FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,

    CONSTRAINT cart_items_product_unique
        UNIQUE (cart_id, product_id)
);



