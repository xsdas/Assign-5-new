package com.stackroute.newz.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;



/* Annotate this class with @Aspect and @Component */
@Aspect
@Component
public class LoggerAspect {

	/*
	 * Write loggers for each of the methods of NewsController, any particular method
	 * will have all the four aspectJ annotation
	 * (@Before, @After, @AfterReturning, @AfterThrowing).
	 */
	private static final Logger logger = LoggerFactory.getLogger(LoggerAspect.class);

	@Pointcut("execution(* com.stackroute.userprofile.controller.*.*(..))")
	public void allControllerMethods() {
	}

	@Before("allControllerMethods()")
	public void beforeAdvice(JoinPoint joinPoint) {
		logger.info("*******Before**********");
		logger.debug("Method" + joinPoint.getSignature().getName() + "started");
	}

	@After("allControllerMethods()")
	public void afterAdvice(JoinPoint joinPoint) {
		logger.info("*******After**********");
		logger.debug("Method" + joinPoint.getSignature().getName() + "over");
	}

	@AfterReturning(value = "allControllerMethods()", returning = "result")
	public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
		logger.info("*******AfterReturning**********");
		logger.debug("Method:" + joinPoint.getSignature().getName());
		logger.debug("return value:", result);
	}

	@AfterThrowing(value = "allControllerMethods()", throwing = "error")
	public void afterThrowingAdvice(JoinPoint joinPoint, Throwable error) {
		logger.info("*******AfterThrowing**********");
		logger.debug("Method:" + joinPoint.getSignature().getName());
		logger.debug("exception:", error);
	}

}
