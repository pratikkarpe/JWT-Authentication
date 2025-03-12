package com.pratik.productservice.aop;

import com.pratik.productservice.model.Product;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Configuration
public class CommonFieldsPopulatorAspect {

    @Before("execution(* com.pratik.productservice.service.*.create*(..)) && args(product)")
    public void beforeExecution(JoinPoint pjp, Product product) throws Throwable {
        product.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME).toString());
        product.setUpdateAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME).toString());
    }

    @Before("execution(* com.pratik.productservice.service.*.update*(..)) && args(product)")
    public void beforeExecutionUpdate(JoinPoint pjp, Product product) throws Throwable {
        product.setUpdateAt(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME).toString());
    }

}
