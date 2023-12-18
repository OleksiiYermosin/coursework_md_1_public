package ua.kpi.receiver.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.kpi.receiver.entity.SensorData;
import ua.kpi.receiver.repository.SensorDataRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class MessagesHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(MessagesHandler.class);

  private final List<SensorData> messages;
  private final SensorDataRepository sensorDataRepository;

  public MessagesHandler(SensorDataRepository sensorDataRepository) {
    this.sensorDataRepository = sensorDataRepository;
    messages = new ArrayList<>();
  }

  public void handleMessage(String message) {
    SensorData entity = parseMessage(message);
    if (!entity.equals(new SensorData())) {
      messages.add(entity);
    }
    if (messages.size() > 15) {
      scheduleFixedDelayTask();
    }
  }

  @Scheduled(fixedDelay = 60, timeUnit = SECONDS)
  public void scheduleFixedDelayTask() {
    LOGGER.atInfo().log("Data save is in progress, batch size = " + messages.size());
    sensorDataRepository.saveAll(messages);
    messages.clear();
  }

  private SensorData parseMessage(String message) {
    String[] columns = message.split(";");
    if (columns.length != 4) {
      LOGGER.atError().log("Invalid message: " + message);
      return new SensorData();
    }
    return SensorData.builder()
        .macAddress(columns[0])
        .timestamp(
            LocalDateTime.parse(
                columns[1], DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")))
        .temperature(Double.valueOf(columns[2].replace(",", ".")))
        .humidity(Double.valueOf(columns[3].replace(",", ".")))
        .build();
  }
}
