package pl.methodListener.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.methodListener.entity.LogRequestEntity;

public interface LogRequestRepository extends JpaRepository<LogRequestEntity, Integer> {
}
