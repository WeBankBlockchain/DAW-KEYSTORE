package com.webank.wsdaw.keystore.aspect;

import cn.hutool.json.JSONUtil;
import com.webank.wsdaw.keystore.enums.CodeEnum;
import com.webank.wsdaw.keystore.monitor.MonitorEntry;
import com.webank.wsdaw.keystore.monitor.MonitorUtil;
import com.webank.wsdaw.keystore.vo.response.CommonResponse;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class WebLogAspect {

    @Around("execution(* com.webank.wsdaw.keystore.controller.*.*(..))")
    public Object logAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                        .getRequest();
        String ipAddress = request.getRemoteAddr();
        log.debug("Request http remote IP is [{}]", ipAddress);
        try {
            CommonResponse result = (CommonResponse) proceedingJoinPoint.proceed();
            MonitorEntry monitorEntry =
                    new MonitorEntry(
                            proceedingJoinPoint.getSignature().getName(),
                            null,
                            String.valueOf(result.getCode()),
                            result.getMsg(),
                            System.currentTimeMillis() - startTime);
            MonitorUtil.getMonitorLogger().info("[{}]", JSONUtil.toJsonStr(monitorEntry));
            return result;
        } catch (Exception e) {
            CodeEnum codeEnum = CodeEnum.UNKNOWN_ERROR;
            MonitorEntry monitorEntry =
                    new MonitorEntry(
                            proceedingJoinPoint.getSignature().getName(),
                            null,
                            String.valueOf(codeEnum.getCode()),
                            codeEnum.getMsg(),
                            System.currentTimeMillis() - startTime);
            MonitorUtil.getMonitorLogger().info("[{}]", JSONUtil.toJsonPrettyStr(monitorEntry));
            throw e;
        }
    }

    @AfterReturning(
            pointcut = "execution(* com.webank.wsdaw.keystore.controller.*.*(..))",
            returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Object[] params = joinPoint.getArgs();
        log.info(
                "Returning from=[{}] path=[{}] method=[{}] classMethod=[{}] params=[{}] result=[{}]",
                request.getRemoteAddr(),
                request.getRequestURI(),
                request.getMethod(),
                joinPoint.getSignature().getDeclaringTypeName()
                        + "."
                        + joinPoint.getSignature().getName(),
                params,
                JSONUtil.toJsonPrettyStr(result));
    }
}
