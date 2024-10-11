package com.example.fashionforecastbackend.global.oauth2;

import java.io.Serializable;

public record UserDetail(
	Long memberId,
	String role
) implements Serializable {
}
