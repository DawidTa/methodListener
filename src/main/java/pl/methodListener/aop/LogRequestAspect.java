package pl.methodListener.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.methodListener.entity.LogRequestEntity;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;

@Aspect
@Service
@RequiredArgsConstructor
public class LogRequestAspect {

    private final ApplicationEventPublisher eventPublisher;

    @Pointcut("@annotation(pl.methodListener.aop.LogRequest)")
    public void addLogRequest() {
    }

    @Around("addLogRequest() && args(.., @RequestBody body)")
    public Object addLogRequest(ProceedingJoinPoint joinPoint, Object body) throws Throwable {

        final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long end = System.currentTimeMillis() - start;

        LogRequestEntity logRequest = setLogRequestEntity(method, end, joinPoint, request, body, proceed);

        eventPublisher.publishEvent(logRequest);
        return proceed;
    }

    private String setHeaders(HttpServletRequest request) {
        StringBuilder headers = new StringBuilder();

        if (request != null) {
            Enumeration<String> allHeaders = request.getHeaderNames();
            while (allHeaders.hasMoreElements()) {
                String headerName = allHeaders.nextElement();
                String headerValue = request.getHeader(headerName);
                headers.append("Name:").append(headerName).append(", Value:").append(headerValue).append(" ;");
            }
        }
        return headers.toString();
    }

    private LogRequestEntity setLogRequestEntity(Method method, long end, ProceedingJoinPoint joinPoint,
                                                 HttpServletRequest request, Object body, Object proceed) throws UnknownHostException {

        String headers = setHeaders(request);

        LogRequestEntity logRequest = new LogRequestEntity();
        logRequest.setMethod(method.getName())
                .setIp(InetAddress.getLocalHost().getHostAddress())
                .setBody(body.toString())
                .setParameters(Arrays.toString(method.getParameters()))
                .setCookies(Arrays.toString(request.getCookies()))
                .setTime(end)
                .setResponseCode(((ResponseEntity) proceed).getStatusCode().toString())
                .setResponseBody((Objects.requireNonNull(((ResponseEntity) proceed).getBody())).toString())
                .setAttributes(Arrays.toString(joinPoint.getArgs()))
                .setHeaders(headers);

        return logRequest;
    }
}
