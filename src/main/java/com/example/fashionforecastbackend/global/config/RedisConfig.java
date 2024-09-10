package com.example.fashionforecastbackend.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.fashionforecastbackend.global.login.domain.RefreshToken;
import com.example.fashionforecastbackend.search.domain.Search;

import lombok.RequiredArgsConstructor;

@EnableCaching
@Configuration
@RequiredArgsConstructor
@EnableRedisRepositories(basePackages = "com.example.fashionforecastbackend.global.login.domain.repository", redisTemplateRef = "refreshTokenRedisTemplate")
public class RedisConfig {

	private final RedisProperties properties;

	/*
	최근 검색어(Search)는 0번 데이터베이스 사용
	 */
	@Bean
	@Primary
	LettuceConnectionFactory connectionFactory1() {
		return createRedisConnectionFactory(0);
	}

	/*
	리프레시 토큰(Refresh token)은 1번 데이터베이스 사용
	 */
	@Bean
	@Qualifier("redisConnectionFactoryForRefreshToken")
	LettuceConnectionFactory connectionFactory2() {
		return createRedisConnectionFactory(1);
	}

	@Bean
	public RedisTemplate<String, Search> searchRedisTemplate(
		RedisConnectionFactory connectionFactory) {

		RedisTemplate<String, Search> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Search.class));
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Search.class));

		return redisTemplate;
	}

	@Bean
	public RedisTemplate<String, RefreshToken> refreshTokenRedisTemplate(
		@Qualifier("redisConnectionFactoryForRefreshToken") RedisConnectionFactory redisConnectionFactory) {

		RedisTemplate<String, RefreshToken> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(RefreshToken.class));
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(RefreshToken.class));

		return redisTemplate;
	}

	public LettuceConnectionFactory createRedisConnectionFactory(final int idx) {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(properties.getHost());
		config.setPort(properties.getPort());
		config.setPassword(properties.getPassword());
		config.setDatabase(idx); // 0번 또는 1번 데이터베이스 지정
		return new LettuceConnectionFactory(config);
	}

}
