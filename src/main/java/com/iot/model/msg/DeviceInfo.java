package com.iot.model.msg;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Data
@Accessors(fluent = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceInfo {

    @JsonAlias({"id"})
    @JsonProperty("dev_id")
    @NotBlank(message = "ID field is missing or blank")
    private String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("ip")
    private String ip;

    @JsonAlias("ver")
    @JsonProperty("version")
    private String version;

    @JsonProperty("hub_id")
    private String hubId;

    @JsonProperty("product_id")
    private String productId;

    @JsonAlias("device_type")
    @JsonProperty("dev_type")
    private String deviceType;

    @JsonProperty("home_id")
    private String homeId;

    @JsonProperty("heartbeat")
    private Instant heartbeat;

    @JsonProperty("expect_hb")
    private Instant expectHeartbeat;
}
