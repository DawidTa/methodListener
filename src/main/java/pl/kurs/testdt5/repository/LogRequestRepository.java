package pl.kurs.testdt5.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kurs.testdt5.entity.LogRequestEntity;

public interface LogRequestRepository extends JpaRepository<LogRequestEntity, Integer> {
}
