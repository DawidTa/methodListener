package pl.kurs.testdt5.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import pl.kurs.testdt5.entity.LogRequestEntity;

import java.lang.reflect.Method;

@Aspect
@Service
public class LogRequestAspect {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Pointcut("execution(* pl.kurs.calculator.service.*.*(..))")
    public void addLogRequest() {
    }

    @Around("addLogRequest()")
    public Object addLogRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        LogRequestEntity logRequest = new LogRequestEntity();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long end = System.currentTimeMillis() - start;

        eventPublisher.publishEvent(logRequest);
        return proceed;
    }
}
