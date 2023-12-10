package ua.kpi.receiver.spark;

import org.apache.spark.sql.*;
import org.springframework.stereotype.Component;
import ua.kpi.receiver.dto.SensorDataDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.to_date;

@Component
public class DataAnalyzer {

  private final SparkSession sparkSession;
  private final Map<String, String> jdbcOptions;

  public DataAnalyzer(SparkSession sparkSession, Map<String, String> jdbcOptions) {
    this.sparkSession = sparkSession;
    this.jdbcOptions = jdbcOptions;
  }

  public List<SensorDataDto> fetchLocationData(
      String location, Optional<LocalDate> startDate, Optional<LocalDate> endDate) {
    Dataset<Row> dataset =
        sparkSession.read().format("jdbc").options(jdbcOptions).option("numPartitions", 4).load();
    dataset
        .select("sensor_id", "location", "humidity", "temperature", "timestamp")
        .where(col("location").equalTo(location));
    filterByDate(dataset, startDate, endDate);
    dataset.distinct();
    return dataset.collectAsList().stream()
        .map(
            r ->
                new SensorDataDto(
                    String.valueOf(r.get(2)),
                    r.isNullAt(3) ? null : r.getDouble(3),
                    r.isNullAt(4) ? null : r.getDouble(4),
                    r.getTimestamp(5).toLocalDateTime()))
        .collect(Collectors.toList());
  }

  private void filterByDate(
      Dataset<Row> dataset, Optional<LocalDate> startDate, Optional<LocalDate> endDate) {
    startDate.ifPresent(
        localDate -> dataset.where(to_date(col("timestamp")).$greater$eq(localDate)));
    endDate.ifPresent(localDate -> dataset.where(to_date(col("timestamp")).$less$eq(localDate)));
  }
}
