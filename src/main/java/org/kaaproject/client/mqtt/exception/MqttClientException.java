package org.kaaproject.client.mqtt.exception;

public class MqttClientException extends RuntimeException {

    public MqttClientException(String message) {
        super(message);
    }

    public MqttClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
