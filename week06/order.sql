CREATE TABLE IF NOT EXISTS demo1.t_order (order_id BIGINT NOT NULL AUTO_INCREMENT,
                                          commodity_id BIGINT NOT NULL,
                                          user_id BIGINT NOT NULL,
                                          order_amount DECIMAL,
                                          order_status INTEGER(1),
    PRIMARY KEY (order_id))ENGINE=InnoDB DEFAULT CHARSET=utf8;
