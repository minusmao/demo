# 删除 fruit 表
DROP TABLE IF EXISTS `fruit`;

# 创建 fruit 表
CREATE TABLE fruit(
    id        INT(10)       NOT NULL AUTO_INCREMENT,
    name      VARCHAR(30)   NOT NULL,
    sale      INT(10)       NOT NULL DEFAULT 0,
    icon_url  VARCHAR(255),
    PRIMARY KEY (id)
)ENGINE=InnoDB;

# 查询 fruit 表
SELECT * FROM fruit;