package ua.kpi.receiver.handler;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.kpi.receiver.entity.SensorData;
import ua.kpi.receiver.repository.SensorDataRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public class MessagesHandler {
  private static final Logger LOGGER = LoggerFactory.getLogger(MessagesHandler.class);
  private static final ObjectMapper OBJECT_MAPPER =
      new ObjectMapper()
          .configure(FAIL_ON_IGNORED_PROPERTIES, false)
          .registerModule(new JavaTimeModule());

  private final List<SensorData> messages;
  private final SensorDataRepository sensorDataRepository;

  public MessagesHandler(SensorDataRepository sensorDataRepository) {
    this.sensorDataRepository = sensorDataRepository;
    messages = new ArrayList<>();
  }

  public void handleMessage(String message) {
    SensorData entity = parseMessage(message);
    LOGGER.atInfo().log("Entity: " + entity);
    if (!entity.equals(new SensorData())) {
      messages.add(entity);
    }
    if (messages.size() > 500) {
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
    try (JsonParser jsonParser = OBJECT_MAPPER.createParser(message)) {
      return validateSensorData(OBJECT_MAPPER.readValue(jsonParser, new TypeReference<>() {}));
    } catch (IOException e) {
      LOGGER.atError().log(String.format("Failed to parse message: %s; cause: %s", message, e));
    }
    return new SensorData();
  }

  private SensorData validateSensorData(SensorData sensorData) {
    if (sensorData.getSensorId() == null
        || sensorData.getLocation() == null
        || sensorData.getTimestamp() == null) {
      return new SensorData();
    }
    return sensorData;
  }
}
