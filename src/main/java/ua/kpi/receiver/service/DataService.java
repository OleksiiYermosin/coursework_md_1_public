package ua.kpi.receiver.service;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Service;
import ua.kpi.receiver.dto.HumidityTemperatureDto;
import ua.kpi.receiver.dto.HumidityTemperatureTimeDto;
import ua.kpi.receiver.dto.SensorDataDto;
import ua.kpi.receiver.entity.Location;
import ua.kpi.receiver.repository.LocationRepository;
import ua.kpi.receiver.spark.DataAnalyzer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.apache.spark.sql.functions.*;
import static org.apache.spark.sql.functions.col;

@Service
public class DataService {

  private final DataAnalyzer dataAnalyzer;
  private final LocationRepository locationRepository;

  public DataService(DataAnalyzer dataAnalyzer, LocationRepository locationRepository) {
    this.dataAnalyzer = dataAnalyzer;
    this.locationRepository = locationRepository;
  }

  public SensorDataDto fetchLocationData(
      String location, Optional<LocalDateTime> startDate, Optional<LocalDateTime> endDate) {
    Dataset<Row> dataset = dataAnalyzer.fetchLocationData(location, startDate, endDate);
    dataset = limitNumberOfRecords(dataset, startDate, endDate);
    List<Row> rows = dataset.collectAsList();
    List<HumidityTemperatureTimeDto> data =
        dataset.collectAsList().stream()
            .map(
                r ->
                    new HumidityTemperatureTimeDto(
                        r.isNullAt(2) ? null : r.getDouble(2),
                        r.isNullAt(1) ? null : r.getDouble(1),
                        r.getTimestamp(3).toLocalDateTime()))
            .sorted(Comparator.comparing(HumidityTemperatureTimeDto::getTimestamp))
            .toList();
    return data.isEmpty()
        ? new SensorDataDto()
        : new SensorDataDto(rows.get(0).getLong(4), rows.get(0).getString(8), data);
  }

  public HumidityTemperatureDto fetchLocationAverageData(
      String location, Optional<LocalDateTime> startDate, Optional<LocalDateTime> endDate) {
    Dataset<Row> dataset = dataAnalyzer.fetchLocationData(location, startDate, endDate);
    dataset = limitNumberOfRecords(dataset, startDate, endDate);
    return new HumidityTemperatureDto(
        dataAnalyzer.fetchAverageLocationData(dataset, "temperature").head().getDouble(0),
        dataAnalyzer.fetchAverageLocationData(dataset, "humidity").head().getDouble(0));
  }

  public List<Location> fetchLocations() {
    return locationRepository.findAll();
  }

  private Dataset<Row> limitNumberOfRecords(
      Dataset<Row> dataset, Optional<LocalDateTime> startDate, Optional<LocalDateTime> endDate) {
    if (startDate.isPresent()
        && endDate.isPresent()
        && Duration.between(startDate.get(), endDate.get()).toSeconds() > 86400) {
      dataset =
          dataAnalyzer.limitNumberOfRecords(
              dataset, col("timestamp").equalTo(date_trunc("day", col("timestamp"))));
    }
    if (startDate.isPresent()
        && endDate.isPresent()
        && Duration.between(startDate.get(), endDate.get()).toSeconds() <= 86400
        && Duration.between(startDate.get(), endDate.get()).toSeconds() > 3600) {
      dataset =
          dataAnalyzer.limitNumberOfRecords(
              dataset, col("timestamp").equalTo(date_trunc("hour", col("timestamp"))));
    }
    return dataset;
  }
}
