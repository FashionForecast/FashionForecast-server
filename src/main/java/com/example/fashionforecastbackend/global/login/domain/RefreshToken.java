package com.example.fashionforecastbackend.global.login.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 14 * 24 * 60 * 60) // 14일
public class RefreshToken {

	@Id
	private String token;
	private String memberId;

}
