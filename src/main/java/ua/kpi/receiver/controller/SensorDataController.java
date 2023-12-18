package ua.kpi.receiver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.kpi.receiver.dto.HumidityTemperatureDto;
import ua.kpi.receiver.dto.SensorDataDto;
import ua.kpi.receiver.entity.Location;
import ua.kpi.receiver.service.DataService;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api", produces = "application/json")
public class SensorDataController {
  private final DataService dataService;

  public SensorDataController(DataService dataService) {
    this.dataService = dataService;
  }

  @GetMapping(path = "/data")
  public SensorDataDto getDataByLocationAndDate(
      @RequestParam("location") String location,
      @RequestParam("startDate") Optional<Long> startDate,
      @RequestParam("endDate") Optional<Long> endDate) {
    return dataService.fetchLocationData(
        location,
        startDate.map(
            v -> Instant.ofEpochMilli(v).atZone(ZoneId.systemDefault()).toLocalDateTime()),
        endDate.map(v -> Instant.ofEpochMilli(v).atZone(ZoneId.systemDefault()).toLocalDateTime()));
  }

  @GetMapping(path = "/average")
  public HumidityTemperatureDto getAverageDataByLocationAndDate(
      @RequestParam("location") String location,
      @RequestParam("startDate") Optional<Long> startDate,
      @RequestParam("endDate") Optional<Long> endDate) {
    return dataService.fetchLocationAverageData(
        location,
        startDate.map(
            v -> Instant.ofEpochMilli(v).atZone(ZoneId.systemDefault()).toLocalDateTime()),
        endDate.map(v -> Instant.ofEpochMilli(v).atZone(ZoneId.systemDefault()).toLocalDateTime()));
  }

  @GetMapping(path = "/rooms")
  public List<Location> listLocations() {
    return dataService.fetchLocations();
  }
}
