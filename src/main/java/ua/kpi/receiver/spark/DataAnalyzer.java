package ua.kpi.receiver.spark;

import org.apache.spark.sql.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.apache.spark.sql.functions.*;

@Component
public class DataAnalyzer {
  private static final String SENSOR_DATA_TABLE = "sensor_data";
  private static final String LOCATIONS_TABLE = "locations";
  private static final String DEVICES_TABLE = "devices";

  private final SparkSession sparkSession;
  private final Map<String, String> jdbcOptions;

  public DataAnalyzer(SparkSession sparkSession, Map<String, String> jdbcOptions) {
    this.sparkSession = sparkSession;
    this.jdbcOptions = jdbcOptions;
  }

  public Dataset<Row> fetchLocationData(
      String locationId, Optional<LocalDateTime> startDate, Optional<LocalDateTime> endDate) {
    Dataset<Row> sensorDataDataset =
        sparkSession
            .read()
            .format("jdbc")
            .options(jdbcOptions)
            .option("numPartitions", 4)
            .option("dbtable", SENSOR_DATA_TABLE)
            .load();
    Dataset<Row> locationsDataset =
        sparkSession
            .read()
            .format("jdbc")
            .options(jdbcOptions)
            .option("numPartitions", 4)
            .option("dbtable", LOCATIONS_TABLE)
            .load();
    Dataset<Row> devicesDataset =
        sparkSession
            .read()
            .format("jdbc")
            .options(jdbcOptions)
            .option("numPartitions", 4)
            .option("dbtable", DEVICES_TABLE)
            .load();
    Dataset<Row> dataset =
        sensorDataDataset
            .select("mac_address", "humidity", "temperature", "timestamp")
            .join(devicesDataset, "mac_address")
            .join(
                locationsDataset,
                locationsDataset.col("id").equalTo(devicesDataset.col("location_id")))
            .where(devicesDataset.col("location_id").equalTo(locationId));
    dataset = filterByDate(dataset, startDate, endDate);
    dataset = dataset.distinct();
    return dataset;
  }

  public Dataset<Row> fetchAverageLocationData(Dataset<Row> dataset, String column) {
    return dataset.agg(avg(column));
  }

  public Dataset<Row> limitNumberOfRecords(Dataset<Row> dataset, Column condition) {
    return dataset.where(condition);
  }

  private Dataset<Row> filterByDate(
      Dataset<Row> dataset, Optional<LocalDateTime> startDate, Optional<LocalDateTime> endDate) {
    dataset =
        startDate.isPresent()
            ? dataset.where(col("timestamp").$greater$eq(startDate.get()))
            : dataset;
    dataset =
        endDate.isPresent() ? dataset.where(col("timestamp").$less$eq(endDate.get())) : dataset;
    return dataset;
  }
}
