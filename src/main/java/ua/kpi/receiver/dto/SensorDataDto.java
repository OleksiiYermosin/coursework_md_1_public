package ua.kpi.receiver.dto;

import java.time.LocalDateTime;

public class SensorDataDto {

  private String location;

  private Double humidity;

  private Double temperature;

  private LocalDateTime timestamp;

  public SensorDataDto() {}

  public SensorDataDto(
      String location, Double humidity, Double temperature, LocalDateTime timestamp) {
    this.location = location;
    this.humidity = humidity;
    this.temperature = temperature;
    this.timestamp = timestamp;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public Double getHumidity() {
    return humidity;
  }

  public void setHumidity(Double humidity) {
    this.humidity = humidity;
  }

  public Double getTemperature() {
    return temperature;
  }

  public void setTemperature(Double temperature) {
    this.temperature = temperature;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }
}
