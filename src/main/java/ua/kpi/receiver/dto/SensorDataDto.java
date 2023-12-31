package ua.kpi.receiver.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SensorDataDto {
  private Long sensorId;
  private String location;
  private List<HumidityTemperatureTimeDto> data;
}
