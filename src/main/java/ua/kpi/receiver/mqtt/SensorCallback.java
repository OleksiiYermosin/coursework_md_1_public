package ua.kpi.receiver.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ua.kpi.receiver.handler.MessagesHandler;

@Component
public class SensorCallback implements MqttCallback {
  private static final Logger LOGGER = LoggerFactory.getLogger(SensorCallback.class);

  private final MessagesHandler messagesHandler;

  public SensorCallback(MessagesHandler messagesHandler) {
    this.messagesHandler = messagesHandler;
  }

  @Override
  public void connectionLost(Throwable throwable) {
    LOGGER.atError().log("Connection has been lost.");
  }

  @Override
  public void messageArrived(String s, MqttMessage mqttMessage) {
    LOGGER.atInfo().log("Message received: " + mqttMessage + "; " + s);
    messagesHandler.handleMessage(mqttMessage.toString());
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {}
}
