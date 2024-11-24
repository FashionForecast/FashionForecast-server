package com.example.fashionforecastbackend.global.login.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 14 * 24 * 60 * 60) // 14일
public class RefreshToken {

	@Id
	private String memberId;
	private String token;

	public void updateToken(String token) {
		this.token = token;
	}
}
