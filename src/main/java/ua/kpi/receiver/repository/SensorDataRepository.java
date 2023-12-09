package ua.kpi.receiver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kpi.receiver.entity.SensorData;

@Repository
public interface SensorDataRepository extends JpaRepository<SensorData, Long> {}
