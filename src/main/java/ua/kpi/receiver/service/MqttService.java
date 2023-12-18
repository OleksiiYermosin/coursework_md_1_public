package ua.kpi.receiver.service;

import jakarta.annotation.PostConstruct;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MqttService {
  private static final Logger LOGGER = LoggerFactory.getLogger(MqttService.class);
  private static final int QOS = 1;

  private final MqttClient mqttClient;
  private final MqttConnectOptions connectionOptions;

  @Value("${mqtt.topic}")
  private String MQTT_SUBSCRIBE_TOPIC;

  public MqttService(MqttClient mqttClient, MqttConnectOptions connectionOptions) {
    this.mqttClient = mqttClient;
    this.connectionOptions = connectionOptions;
  }

  @PostConstruct
  public void subscribeToTopic() throws MqttException {
    if (!mqttClient.isConnected()) {
      connect();
    }
    LOGGER.atInfo().log("Topic: " + MQTT_SUBSCRIBE_TOPIC);
    mqttClient.subscribe(MQTT_SUBSCRIBE_TOPIC, QOS);
  }

  private void connect() {
    while (!mqttClient.isConnected()) {
      try {
        mqttClient.connect(connectionOptions);
      } catch (MqttException e) {
        LOGGER.atError().log("MQTT connection attempt failed: " + e);
      }
    }
    LOGGER.atInfo().log("Connection established");
  }
}
