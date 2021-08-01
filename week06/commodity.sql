CREATE TABLE IF NOT EXISTS demo1.t_commodity (commodity_id BIGINT NOT NULL AUTO_INCREMENT,
                                              commodity_name VARCHAR(50),
    commodity_type VARCHAR(10),
    commodity_description VARCHAR(1000),
    commodity_weight INTEGER,
    commodity_status INTEGER(1),
    PRIMARY KEY (commodity_id))ENGINE=InnoDB DEFAULT CHARSET=utf8;