package ua.kpi.receiver.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MqttSubscriber {
  private static final Logger LOGGER = LoggerFactory.getLogger(MqttSubscriber.class);
  private static final int QOS = 1;

  private final MqttClient mqttClient;
  private final MqttConnectOptions connectionOptions;

  @Value("${mqtt.topic}")
  private String MQTT_SUBSCRIBE_TOPIC;

  public MqttSubscriber(
      MqttClient mqttClient, MqttConnectOptions connectionOptions, SensorCallback sensorCallback) {
    this.mqttClient = mqttClient;
    this.connectionOptions = connectionOptions;
    // Set callback before subscribing to the topic to get unreceived messages.
    mqttClient.setCallback(sensorCallback);
  }

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
