package ua.kpi.receiver.mqtt;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.stereotype.Component;

@Component
public class MqttMessageListener implements Runnable {
  private final MqttSubscriber subscriber;

  public MqttMessageListener(MqttSubscriber subscriber) {
    this.subscriber = subscriber;
  }

  @Override
  public void run() {
    try {
      subscriber.subscribeToTopic();
    } catch (MqttException e) {
      throw new RuntimeException(e);
    }
  }
}
