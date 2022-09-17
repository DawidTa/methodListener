package pl.methodListener.event;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.methodListener.entity.LogRequestEntity;
import pl.methodListener.repository.LogRequestRepository;

@Service
@RequiredArgsConstructor
public class LogRequestListener {

    private final LogRequestRepository logRequestRepository;

    @Async
    @EventListener(LogRequestEntity.class)
    public void saveToDb(LogRequestEntity logRequestEntity) {
        logRequestRepository.save(logRequestEntity);
    }
}
