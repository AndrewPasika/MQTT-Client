package org.kaaproject.client.mqtt;

import lombok.SneakyThrows;
import lombok.experimental.Delegate;

import org.eclipse.paho.client.mqttv3.MqttClient;

public class MqttKpClient {

    @Delegate
    private final MqttClient MQTT_CLIENT;

    @SneakyThrows
    public MqttKpClient(String serverUri, String clientId) {
        MQTT_CLIENT = new MqttClient(serverUri, clientId);
        MQTT_CLIENT.connect();
    }
}
