package org.kaaproject.client.mqtt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

import org.apache.commons.lang3.RandomUtils;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String SERVER_URI = "tcp://localhost:30720";
    private static final String CLIENT_ID = "mqtt-kp-client";

    private static final String TOPIC = "kp1/irrigation-ver-1/dcx_instance_1/epToken-1/json";

    @SneakyThrows
    public static void main(String[] args) {
        MqttKpClient client = new MqttKpClient(SERVER_URI, CLIENT_ID);
        client.publish(TOPIC, generateMqttMessage());
    }

    @SneakyThrows
    private static MqttMessage generateMqttMessage() {
        MqttMessage mqttMessage = new MqttMessage();
        mqttMessage.setId(RandomUtils.nextInt());
        mqttMessage.setQos(1);
        mqttMessage.setPayload(getPayload("/payload.json"));
        return mqttMessage;
    }

    @SneakyThrows
    private static byte[] getPayload(String filename) {
        Path path = Paths.get(Main.class.getResource(filename).toURI());
        String payload = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
        JsonNode node = OBJECT_MAPPER.readTree(payload);
        return OBJECT_MAPPER.writeValueAsBytes(node);
    }
}
