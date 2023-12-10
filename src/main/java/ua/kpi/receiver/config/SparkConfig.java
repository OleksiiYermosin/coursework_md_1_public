package ua.kpi.receiver.config;

import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SparkConfig {
  private static final String TABLE_NAME = "sensors_data";

  @Value("${spring.datasource.url}")
  private String datasourceUrl;

  @Value("${driver}")
  private String jdbcDriver;

  @Value("${spring.datasource.username}")
  private String user;

  @Value("${spring.datasource.password}")
  private String password;

  @Bean
  public SparkSession provideSparkSession() {
    return SparkSession.builder().master("local[4]").appName("ZalupaSpark").getOrCreate();
  }

  @Bean
  public Map<String, String> provideJdbcOptions() {
    return Map.of(
        "url",
        datasourceUrl,
        "driver",
        jdbcDriver,
        "dbtable",
        TABLE_NAME,
        "user",
        user,
        "password",
        password);
  }
}
