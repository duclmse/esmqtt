CREATE TABLE IF NOT EXISTS device (
    `id`          VARCHAR(50) PRIMARY KEY,
    `name`        VARCHAR(50),
    `ip`          VARCHAR(15),
    `version`     VARCHAR(15),
    `hub_id`      VARCHAR(50),
    `product_id`  VARCHAR(50),
    `device_type` VARCHAR(50),
    `home_id`     VARCHAR(50),
    `heartbeat`   DATETIME,
    `expect_hb`   DATETIME
);

CREATE TABLE IF NOT EXISTS device_message_history (
    `device_id` VARCHAR(50),
    `ts`        DATETIME,
    `message`   TEXT,
    PRIMARY KEY (device_id, ts),
    FOREIGN KEY device_message (device_id) REFERENCES device (id)
);

CREATE TABLE IF NOT EXISTS device_command_history (
    `device_id` VARCHAR(50),
    `ts`        DATETIME,
    `message`   TEXT,
    PRIMARY KEY (device_id, ts),
    FOREIGN KEY device_command (device_id) REFERENCES device (id)
);

CREATE TABLE IF NOT EXISTS api_history (
    ts       DATETIME,
    endpoint VARCHAR(50),
    method   VARCHAR(10),
    body     TEXT,
    PRIMARY KEY (ts, endpoint, method)
);

CREATE TABLE IF NOT EXISTS device_status_history (
    `device_id`       VARCHAR(50),
    `ts`              DATETIME,
    `switch_1`        BOOL,
    `countdown_1`     INT,
    `add_ele`         INT,
    `cur_current`     INT,
    `cur_power`       INT,
    `cur_voltage`     INT,
    `test_bit`        INT,
    `voltage_coe`     INT,
    `electric_coe`    INT,
    `power_coe`       INT,
    `electricity_coe` INT,
    `fault`           VARCHAR(10),
    `relay_status`    VARCHAR(10),
    `cycle_time`      VARCHAR(50),
    `random_time`     VARCHAR(50),
    PRIMARY KEY (device_id, ts),
    FOREIGN KEY device_command (device_id) REFERENCES device (id)
);

CREATE TABLE IF NOT EXISTS firmware_info (
    `firmware_version` VARCHAR(10),
    `hardware_version` VARCHAR(10),
    `url`              VARCHAR(2048),
    `location`         VARCHAR(2048),
    `hash`             VARCHAR(1000),
    `update_ts`        DATETIME,
    PRIMARY KEY (firmware_version, hardware_version)
);
