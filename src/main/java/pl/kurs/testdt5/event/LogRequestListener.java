package pl.kurs.testdt5.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.kurs.testdt5.entity.LogRequestEntity;
import pl.kurs.testdt5.repository.LogRequestRepository;

@Service
@RequiredArgsConstructor
public class LogRequestListener {

    private final LogRequestRepository logRequestRepository;

    @EventListener(LogRequestEntity.class)
    public void saveToDb(LogRequestEntity logRequestEntity) {
        logRequestRepository.save(logRequestEntity);
    }
}
