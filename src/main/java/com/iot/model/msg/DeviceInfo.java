package com.iot.model.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class DeviceInfo {

    @JsonProperty("dev_id")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("ip")
    private String ip;

    @JsonProperty("ver")
    private String version;

    @JsonProperty("hub_id")
    private String hubId;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("dev_type")
    private String deviceType;

    @JsonProperty("home_id")
    private String homeId;

}
