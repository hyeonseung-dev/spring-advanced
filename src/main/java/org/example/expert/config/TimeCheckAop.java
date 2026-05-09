package org.example.expert.config;

import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class TimeCheckAop {

	private final ObjectMapper objectMapper;

	public TimeCheckAop(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	@Around(
		"execution(* org.example.expert.domain.user.controller.UserAdminController.*(..)) || " +
		"execution(* org.example.expert.domain.comment.controller.CommentAdminController.*(..))"
	)
	public Object executionTime(ProceedingJoinPoint joinPoint) throws Throwable {

		HttpServletRequest request =
			((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();

		long start = System.currentTimeMillis();

		LocalDateTime now = LocalDateTime.now();

		long userId = (long)request.getAttribute("userId");

		String url = String.valueOf(request.getRequestURL());

		Object[] args = joinPoint.getArgs();

		log.info("[AOP] 요청한 사용자 Id = {}, API 요청 시각 = {}, API 요청 URL = {}" , userId, now, url);

		for (Object arg : args) {

			if (!(arg instanceof HttpServletRequest)
				&& !(arg instanceof HttpServletResponse)) {

				log.info("[AOP] requestBody={}",
					objectMapper.writeValueAsString(arg));
			}
		}

		Object result = joinPoint.proceed();

		long end = System.currentTimeMillis();

		log.info("[AOP] responseBody={}", objectMapper.writeValueAsString(result));
		log.info("[AOP] {} 실행됨 in {}ms" , joinPoint.getSignature() , end - start);

		return result;
	}
}
