package com.iot;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TestMqtt {


    @Test
    public void testMqttConnection() {
        try (var client = new MqttClient("tcp://localhost:1883", "fghfghf")) {
            client.connect(mqttConnectOptions());
            MqttMessage msg = new MqttMessage();
            msg.setPayload("payload".getBytes());
            msg.setQos(1);
            msg.setRetained(true);

            client.publish("device/test/topic", msg);


        } catch (MqttException e) {
            log.info("{}", e);
        }
    }

    public MqttConnectOptions mqttConnectOptions() {
        return new MqttConnectOptions();
    }

    @Test
    public void testBoolean() throws IOException {
        Boolean a = null;
        var s = a ? "okie" : "f";
        System.out.println(s);
    }

    @Test
    public void testParsingJson() throws IOException {
        var mapper = new ObjectMapper();
        var payload = ("").getBytes(StandardCharsets.UTF_8);
        var o = mapper.readValue(payload, new TypeReference<Map<String,Object>>(){});
        log.info("{}", o);
        log.info("{}", mapper.writeValueAsString(o));
    }

    @Test
    public final void givenTypeHasFilterThatIgnoresFieldByName_whenDtoIsSerialized_thenCorrect() throws IOException {

        var mapper = new ObjectMapper();
        var theFilter = SimpleBeanPropertyFilter.serializeAllExcept("intValue");
        var filters = new SimpleFilterProvider().addFilter("myFilter", theFilter);

        MyDtoWithFilter dtoObject = new MyDtoWithFilter();
        String dtoAsString = mapper.writer(filters).writeValueAsString(dtoObject);

        Assertions.assertFalse(dtoAsString.contains("intValue"));
        Assertions.assertTrue(dtoAsString.contains("booleanValue"));
        Assertions.assertTrue(dtoAsString.contains("stringValue"));
        System.out.println(dtoAsString);
    }

    @JsonFilter("myFilter")
    public static class MyDtoWithFilter {

    }

    @Test
    public void test() {
        var s = "automation";
        var map = new HashMap<Character, Integer>();
        for (int i = 0, l = s.length(); i < l; i++) {
            map.compute(s.charAt(i), (k, v) -> v == null ? 1 : v + 1);
        }
        System.out.println(map);
        map.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> System.out.printf("%c%d", entry.getKey(), entry.getValue()));
        System.out.println();
    }
}
