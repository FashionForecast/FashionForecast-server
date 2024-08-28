package com.example.fashionforecastbackend.global.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.example.fashionforecastbackend.search.domain.Search;

import lombok.RequiredArgsConstructor;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class RedisConfig {


	private final RedisProperties properties;

	/*
	최근 검색어(Search)는 0번 데이터베이스 사용
	 */
	@Bean
	public RedisConnectionFactory redisConnectionFactoryForSearch() {
		RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
		config.setHostName(properties.getHost());
		config.setPort(properties.getPort());
		config.setPassword(properties.getPassword());
		config.setDatabase(0); // SELECT 0번 데이터베이스
		return new LettuceConnectionFactory(config);
	}

	@Bean
	public RedisTemplate<String, Search> searchRedisTemplate() {

		RedisTemplate<String, Search> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactoryForSearch());
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Search.class));
		redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Search.class));

		return redisTemplate;
	}


}
