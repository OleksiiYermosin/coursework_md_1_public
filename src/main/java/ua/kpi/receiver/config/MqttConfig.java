package ua.kpi.receiver.config;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MqttConfig {
  private static final String CLIENT_ID = "SpringClient";

  @Value("${mqtt.broker}")
  private String MQTT_BROKER;

  @Value("${mqtt.port}")
  private int MQTT_PORT;

  @Value("${mqtt.username}")
  private String MQTT_USERNAME;

  @Value("${mqtt.password}")
  private String MQTT_PASSWORD;

  @Bean
  public MqttConnectOptions provideMqttConnectOptions() {
    MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
    mqttConnectOptions.setUserName(MQTT_USERNAME);
    mqttConnectOptions.setPassword(MQTT_PASSWORD.toCharArray());
    mqttConnectOptions.setCleanSession(false);
    mqttConnectOptions.setAutomaticReconnect(true);
    return mqttConnectOptions;
  }

  @Bean
  public MqttClient provideMqttClient() throws MqttException {
    return new MqttClient(String.format("tcp://%s:%d", MQTT_BROKER, MQTT_PORT), CLIENT_ID);
  }
}
