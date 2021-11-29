package pl.kurs.testdt5.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.kurs.testdt5.entity.LogRequestEntity;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;

@Aspect
@Service
public class LogRequestAspect {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Pointcut("@annotation(pl.kurs.testdt5.aop.LogRequest)")
    public void addLogRequest() {
    }

    @Around("addLogRequest() && args(.., @RequestBody body)")
    public Object addLogRequest(ProceedingJoinPoint joinPoint, Object body) throws Throwable {
        StringBuilder headers = new StringBuilder();

        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        LogRequestEntity logRequest = new LogRequestEntity();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long end = System.currentTimeMillis() - start;

        if (request != null) {
            Enumeration<String> allHeaders = request.getHeaderNames();
            while (allHeaders.hasMoreElements()) {
                String headerName = allHeaders.nextElement();
                String headerValue = request.getHeader(headerName);
                headers.append("Name:").append(headerName).append(", Value:").append(headerValue).append(" ;");
            }
        }

        logRequest.setMethod(method.getName())
                .setIp(InetAddress.getLocalHost().getHostAddress())
                .setBody(body.toString())
                .setParameters(Arrays.toString(method.getParameters()))
                .setCookies(Arrays.toString(request.getCookies()))
                .setTime(end)
                .setResponseCode(((ResponseEntity) proceed).getStatusCode().toString())
                .setResponseBody((Objects.requireNonNull(((ResponseEntity) proceed).getBody())).toString())
                //.setAttributes(Arrays.toString(joinPoint.getArgs()))
                .setHeaders(headers.toString());

        eventPublisher.publishEvent(logRequest);
        return proceed;
    }
}
