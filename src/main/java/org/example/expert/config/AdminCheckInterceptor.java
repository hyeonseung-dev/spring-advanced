package org.example.expert.config;

import java.io.IOException;
import java.time.LocalDateTime;

import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminCheckInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

		// 1. 현재 로그인 한 사용자 권한 꺼내기
		// JwtFilter 에서 set 한 userRole 값을 가져옴
		String role = (String) request.getAttribute("userRole");

		if (role == null) {
			log.warn("권한 정보 없음. method={}, uri={}", request.getMethod(), request.getRequestURI());
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증이 필요합니다.");
			return false;
		}

		// 2. 권한이 관리자인지 확인
		if(!"ADMIN".equals(role)){
			log.warn("관리자 권한 없음. 접근 거부");
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "관리자 권한이 필요합니다.");
			return false;
		}
		// 3. 인증 성공 시 요청 시각과 URL 로깅
		String url = String.valueOf(request.getRequestURL());

		log.info("관리자 요청 허용. url={}, time={}", url, LocalDateTime.now());

		return true;
	}
}
