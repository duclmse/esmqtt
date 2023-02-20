package com.iot.repository.iml;

import com.iot.model.CommandInfo;
import com.iot.repository.interfaces.DeviceControlRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceControlRepositoryImpl implements DeviceControlRepository {

   private final JdbcTemplate jdbc;

   @Override
    public int saveCommand(CommandInfo info) {
       return 0;
   }

}
