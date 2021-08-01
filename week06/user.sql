CREATE TABLE IF NOT EXISTS demo1.t_user (user_id BIGINT NOT NULL AUTO_INCREMENT,
                                         user_name VARCHAR(50),
    user_password VARCHAR(50),
    user_nickName VARCHAR(50),
    user_identifier VARCHAR(20),
    PRIMARY KEY (user_id))ENGINE=InnoDB DEFAULT CHARSET=utf8;