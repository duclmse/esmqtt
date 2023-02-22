package com.iot.repository.iml;

import com.iot.model.msg.DeviceInfo;
import com.iot.repository.interfaces.DeviceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DeviceRepositoryImpl implements DeviceRepository {

    private final JdbcTemplate jdbc;

    @Override
    public int createDevice(DeviceInfo i) {
        var sql = "INSERT INTO device(`id`, `name`, `ip`, `version`, `hub_id`, `product_id`, `device_type`, `home_id`) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        return jdbc.update(sql, i.id(), i.name(), i.ip(), i.version(), i.hubId(), i.productId(), i.deviceType(),
                i.homeId());
    }

    @Override
    public DeviceInfo readDevice(String id) {
        var sql = "SELECT * FROM device WHERE id = ?;";
        return jdbc.query(sql, this::map, id);
    }

    @Override
    public List<DeviceInfo> readDevices(String id) {
        var sql = "SELECT * FROM device;";
        return jdbc.query(sql, (rs, i) -> this.map(rs));
    }

    @Override
    public int updateDevice(String id, DeviceInfo info) {
        var sql = "UPDATE device SET name = ? WHERE id = ?;";
        return jdbc.update(sql, info.name(), id);
    }

    @Override
    public int deleteDevice(String id) {
        var sql = "DELETE FROM device WHERE id = ?;";
        return jdbc.update(sql, id);
    }

    private DeviceInfo map(ResultSet rs) throws SQLException {
        return new DeviceInfo()
                .id(rs.getString("id"))
                .name(rs.getString("name"))
                .ip(rs.getString("ip"))
                .version(rs.getString("version"))
                .hubId(rs.getString("hub_id"))
                .productId(rs.getString("product_id"))
                .deviceType(rs.getString("device_type"))
                .homeId(rs.getString("home_id"));
    }
}
