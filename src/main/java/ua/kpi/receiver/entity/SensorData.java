package ua.kpi.receiver.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "sensors_data")
public class SensorData {

  @Id
  @Column(name = "record_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long recordId;

  @Column(name = "sensor_id")
  private Long sensorId;

  @Column(name = "location")
  private String location;

  @Column(name = "humidity")
  private Double humidity;

  @Column(name = "temperature")
  private Double temperature;

  @Column(name = "timestamp")
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", shape = JsonFormat.Shape.STRING)
  private LocalDateTime timestamp;

  public SensorData() {}

  public SensorData(
      long recordId,
      long sensorId,
      String location,
      double humidity,
      double temperature,
      LocalDateTime timestamp) {
    this.recordId = recordId;
    this.sensorId = sensorId;
    this.location = location;
    this.humidity = humidity;
    this.temperature = temperature;
    this.timestamp = timestamp;
  }

  public Long getRecordId() {
    return recordId;
  }

  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }

  public Long getSensorId() {
    return sensorId;
  }

  public void setSensorId(Long sensorId) {
    this.sensorId = sensorId;
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    SensorData that = (SensorData) o;
    return recordId == that.recordId
        && sensorId == that.sensorId
        && Double.compare(humidity, that.humidity) == 0
        && Double.compare(temperature, that.temperature) == 0
        && Objects.equals(location, that.location)
        && Objects.equals(timestamp, that.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(recordId, sensorId, location, humidity, temperature, timestamp);
  }

  @Override
  public String toString() {
    return "SensorData{"
        + "recordId="
        + recordId
        + ", sensorId="
        + sensorId
        + ", location='"
        + location
        + '\''
        + ", humidity="
        + humidity
        + ", temperature="
        + temperature
        + ", timestamp="
        + timestamp
        + '}';
  }
}
