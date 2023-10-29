package org.wallet_service.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Класс-аспект выодящий информацию о времени выполнения, отмеченного аннотацией @Time, метода.
 */
@Aspect
@Component
public class TimeAspect {
    @Pointcut("within(@org.wallet_service.aspect.Time *) && execution(* *(..))")
    public void annotatedByTime() {}

    /**
     * Метод высчитывает время работы, отмеченного аннотацией @Time, метода.
     */
    @Around("annotatedByTime()")
    public Object time(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println("Execution of method " + proceedingJoinPoint.getSignature() +
                " finished. Execution time is " + (endTime - startTime) + " ms");
        return result;
    }
}
