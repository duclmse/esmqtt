CREATE TABLE IF NOT EXISTS device (
    `id`          VARCHAR(50) PRIMARY KEY,
    `name`        VARCHAR(50),
    `ip`          VARCHAR(15),
    `version`     VARCHAR(15),
    `hub_id`      VARCHAR(50),
    `product_id`  VARCHAR(50),
    `device_type` VARCHAR(50),
    `home_id`     VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS device_message_history (
    `device_id` VARCHAR(50),
    `ts`        TIMESTAMP,
    `message`   TEXT,
    PRIMARY KEY (device_id, ts),
    FOREIGN KEY device_message (device_id) REFERENCES device (id)
);

CREATE TABLE IF NOT EXISTS device_command_history (
    `device_id` VARCHAR(50),
    `ts`        TIMESTAMP,
    `message`   TEXT,
    PRIMARY KEY (device_id, ts),
    FOREIGN KEY device_command (device_id) REFERENCES device (id)
);
