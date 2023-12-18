package ua.kpi.receiver.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sensor_data")
public class SensorData {

  @Id
  @Column(name = "record_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long recordId;

  @Column(name = "mac_address")
  private String macAddress;

  @Column(name = "humidity")
  private Double humidity;

  @Column(name = "temperature")
  private Double temperature;

  @Column(name = "timestamp")
  private LocalDateTime timestamp;
}
