package com.example.fashionforecastbackend.global.config;

import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.fashionforecastbackend.global.oauth2.handler.Oauth2LoginFailureHandler;
import com.example.fashionforecastbackend.global.oauth2.handler.Oauth2LoginSuccessHandler;
import com.example.fashionforecastbackend.global.oauth2.service.CustomOauth2UserService;

import lombok.RequiredArgsConstructor;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

	private final CustomOauth2UserService customOauth2UserService;
	private final Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;
	private final Oauth2LoginFailureHandler oauth2LoginFailureHandler;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable);


		http
			.authorizeHttpRequests(auth -> auth
				.requestMatchers("/api/v1/member/**").authenticated()
				.anyRequest().permitAll());

		http.oauth2Login(oauth2 -> oauth2
			.userInfoEndpoint(userInfo -> userInfo
				.userService(customOauth2UserService))
			.successHandler(oauth2LoginSuccessHandler)
			.failureHandler(oauth2LoginFailureHandler));

		http
			.cors(cors -> cors.configurationSource(getCorsConfiguration()));

		return http.build();
	}

	CorsConfigurationSource getCorsConfiguration() {

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOriginPatterns(
			List.of("https://*.fashion-forecast.pages.dev", "https://fashion-forecast.pages.dev",
				"http://localhost:3000", "http://localhost:5173", "https://localhost:5173"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setMaxAge(3600L);
		configuration.setExposedHeaders(List.of("Set-Cookie"));

		// configuration.setExposedHeaders(Collections.singletonList("Authorization"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}