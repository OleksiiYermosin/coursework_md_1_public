package ua.kpi.receiver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HumidityTemperatureTimeDto {
  private Double temperature;
  private Double humidity;
  private LocalDateTime timestamp;
}
