package com.example.fashionforecastbackend.global.config;

import com.example.fashionforecastbackend.oauth2.service.Impl.CustomOAuth2UserServiceImpl;
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

@EnableWebSecurity
@Configuration
public class SecurityConfig {
	private final CustomOAuth2UserServiceImpl customOAuth2UserServiceImpl;

	public SecurityConfig(CustomOAuth2UserServiceImpl customOAuth2UserServiceImpl) {
		this.customOAuth2UserServiceImpl = customOAuth2UserServiceImpl;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http
			.csrf(AbstractHttpConfigurer::disable)
			.formLogin(AbstractHttpConfigurer::disable)
			.httpBasic(AbstractHttpConfigurer::disable)
					.oauth2Login((oauth2) -> oauth2
							.userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
									.userService(customOAuth2UserServiceImpl))));
		http
			.authorizeHttpRequests(auth -> auth
				.anyRequest().permitAll());

		http
			.cors(cors -> cors.configurationSource(getCorsConfiguration()));

		return http.build();
	}

	CorsConfigurationSource getCorsConfiguration() {

		CorsConfiguration configuration = new CorsConfiguration();

		configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:5173"));
		configuration.setAllowedMethods(Collections.singletonList("*"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(Collections.singletonList("*"));
		configuration.setMaxAge(3600L);

		// configuration.setExposedHeaders(Collections.singletonList("Authorization"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}

