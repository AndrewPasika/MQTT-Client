package org.kaaproject.client.mqtt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.RandomUtils;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Main {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private static final String SERVER_URI = "tcp://localhost:30720";
  private static final String CLIENT_ID = "mqtt-kp-client";

  private static final String TOPIC = "kp1/appSensor.0.1/dcx_instance_1/7jtt2mzwJR/json/";
  public static final MqttKpClient client = new MqttKpClient(SERVER_URI, CLIENT_ID);

  @SneakyThrows
  public static void main(String[] args) {
    while (true)
    {
      client.publish(TOPIC, generateMqttMessage());
      System.out.println("The message for topic: " + TOPIC + " was published");
      sleepForSeconds(1);
    }

  }

  @SneakyThrows
  private static MqttMessage generateMqttMessage() {
    MqttMessage mqttMessage = new MqttMessage();
    mqttMessage.setId(RandomUtils.nextInt());
    mqttMessage.setQos(1);
    mqttMessage.setPayload(generatePayload("/payload.json"));
    return mqttMessage;
  }

  @SneakyThrows
  private static byte[] generatePayload(String filename) {
    Path path = Paths.get(Main.class.getResource(filename).toURI());
    String unformattedPayload = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    String formattedPayload = String.format(unformattedPayload, OffsetDateTime.now().toString(), genTemperature(), genOilLevel(), generateRevolutions());
    JsonNode node = OBJECT_MAPPER.readTree(formattedPayload);
    return OBJECT_MAPPER.writeValueAsBytes(node);
  }

  private static double generateRevolutions() {
    long t = System.currentTimeMillis() / 1000;
    double result = 20 * ((0.6 * (Math.random() - 0.5) + Math.sin((2 * Math.PI * t) / 90)) * 25 + 90);
    System.out.println("Rev = " + result);
    return result;
  }

  private static double genOilLevel() {
    long t = System.currentTimeMillis() / 1000;
    double result = 10*Math.abs(Math.sin((2*Math.PI * t / 90))) + 20;
    System.out.println("Oil = " + result);
    return result;
  }

  private static double genTemperature() {
    long t = System.currentTimeMillis() / 1000;
    double result = (0.6 * (Math.random() - 0.5) + Math.sin((2 * Math.PI * t) / 90)) * 25 + 100;
    System.out.println("Temp = " + result);
    return result;
  }

  public static void sleepForSeconds(int seconds) {
    try {
      TimeUnit.SECONDS.sleep(seconds);
    } catch (InterruptedException e) {
      System.out.println("Interrupted when thread was sleep for timeout");
    }
  }

}
