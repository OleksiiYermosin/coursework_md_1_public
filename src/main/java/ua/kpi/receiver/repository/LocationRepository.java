package ua.kpi.receiver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kpi.receiver.entity.Location;

public interface LocationRepository extends JpaRepository<Location, Long> {}
