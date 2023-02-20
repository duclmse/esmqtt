package com.iot.model.msg;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true)
public class Header {
    @JsonProperty
    private String name;

    @JsonProperty
    private String ip;

    @JsonProperty
    private String ver;

    @JsonProperty("hub_id")
    private String hubId;

    @JsonProperty("product_id")
    private String productId;

    @JsonProperty("dev_type")
    private String devType;

    @JsonProperty("home_id")
    private String homeId;

    @JsonProperty("dev_id")
    private String devId;
}
