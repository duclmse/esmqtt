package com.iot.model.msg;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DeviceStatus {

    @JsonProperty("1")
    @JsonAlias({"switch_1"})
    private boolean switch1;

    @JsonProperty("9")
    @JsonAlias({"countdown_1"})
    private int countdown1;

    @JsonProperty("17")
    @JsonAlias({"add_ele"})
    private int addEle;

    @JsonProperty("18")
    @JsonAlias({"cur_current"})
    private int curCurrent;

    @JsonProperty("19")
    @JsonAlias({"cur_power"})
    private int curPower;

    @JsonProperty("20")
    @JsonAlias({"cur_voltage"})
    private int curVoltage;

    @JsonProperty("21")
    @JsonAlias({"test_bit"})
    private int testBit;

    @JsonProperty("22")
    @JsonAlias({"voltage_coe"})
    private int voltageCoe;

    @JsonProperty("23")
    @JsonAlias({"electric_coe"})
    private int electricCoe;

    @JsonProperty("24")
    @JsonAlias({"power_coe"})
    private int powerCoe;

    @JsonProperty("25")
    @JsonAlias({"electricity_coe"})
    private int electricityCoe;

    @JsonProperty("26")
    @JsonAlias({"fault"})
    private String fault;

    @JsonProperty("38")
    @JsonAlias({"relay_status"})
    private String relayStatus;

    @JsonProperty("41")
    @JsonAlias({"cycle_time"})
    private String cycleTime;

    @JsonProperty("42")
    @JsonAlias({"random_time"})
    private String randomTime;
}
