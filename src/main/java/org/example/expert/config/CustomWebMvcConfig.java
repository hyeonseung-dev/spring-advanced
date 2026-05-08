package org.example.expert.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CustomWebMvcConfig implements WebMvcConfigurer {
	private final AuthUserArgumentResolver authUserArgumentResolver;

	public CustomWebMvcConfig(AuthUserArgumentResolver authUserArgumentResolver) {
		this.authUserArgumentResolver = authUserArgumentResolver;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(authUserArgumentResolver);
	}
}
