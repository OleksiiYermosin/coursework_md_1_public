package ua.kpi.receiver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.kpi.receiver.dto.SensorDataDto;
import ua.kpi.receiver.spark.DataAnalyzer;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/data", produces = "application/json")
public class SensorDataController {
  private final DataAnalyzer dataAnalyzer;

  public SensorDataController(DataAnalyzer dataAnalyzer) {
    this.dataAnalyzer = dataAnalyzer;
  }

  @GetMapping(path = "/location")
  public Iterable<SensorDataDto> getDataByLocationAndDate(
      @RequestParam("location") String location,
      @RequestParam("startDate") Optional<LocalDate> startDate,
      @RequestParam("endDate") Optional<LocalDate> endDate) {
    return dataAnalyzer.fetchLocationData(location, startDate, endDate);
  }
}
