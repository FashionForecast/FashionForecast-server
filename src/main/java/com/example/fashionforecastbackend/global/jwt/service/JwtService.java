package com.example.fashionforecastbackend.global.jwt.service;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;
import static org.springframework.http.HttpHeaders.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import com.example.fashionforecastbackend.global.error.exception.ExpiredPeriodJwtException;
import com.example.fashionforecastbackend.global.error.exception.InvalidJwtException;
import com.example.fashionforecastbackend.global.login.domain.MemberTokens;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;

@Getter
@Component
public class JwtService {

	private static final String EMPTY_SUBJECT = "";
	private static final String REFRESH_COOKIE_PREFIX = "refresh_token";
	private static final String ACCESS_COOKIE_PREFIX = "access_token";

	private final SecretKey secretKey;
	private final Long accessExpirationTime;
	private final Long refreshExpirationTime;

	public JwtService(
		@Value("${security.jwt.secret-key}") final String secretKey,
		@Value("${security.jwt.access-expiration-time}") final Long accessExpirationTime,
		@Value("${security.jwt.refresh-expiration-time}") final Long refreshExpirationTime
	) {
		this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
		this.accessExpirationTime = accessExpirationTime;
		this.refreshExpirationTime = refreshExpirationTime;
	}

	public MemberTokens generateLoginToken(HttpServletResponse response, final String subject) {
		final String refreshToken = createToken(EMPTY_SUBJECT, refreshExpirationTime);
		final String accessToken = createToken(subject, accessExpirationTime);

		response.addHeader(SET_COOKIE,
			createResponseCookie(refreshToken, REFRESH_COOKIE_PREFIX, refreshExpirationTime).toString());
		response.addHeader(SET_COOKIE,
			createResponseCookie(accessToken, ACCESS_COOKIE_PREFIX, accessExpirationTime).toString());

		return new MemberTokens(accessToken, refreshToken);
	}

	public void validateTokens(final MemberTokens memberTokens) {
		validateRefreshToken(memberTokens.getRefreshToken());
		validateAccessToken(memberTokens.getAccessToken());

	}

	public boolean isValidRefreshAndInvalidAccess(final String refreshToken, final String accessToken) {
		validateRefreshToken(refreshToken);
		try {
			validateAccessToken(accessToken);
		} catch (final ExpiredPeriodJwtException e) {
			return true;
		}
		return false;
	}

	public boolean isValidRefreshAndValidAccess(final String refreshToken, final String accessToken) {
		try {
			validateRefreshToken(refreshToken);
			validateAccessToken(accessToken);
			return true;
		} catch (final JwtException e) {
			return false;
		}
	}

	public String regenerateAccessToken(final String subject) {
		return createToken(subject, accessExpirationTime);
	}

	public String getSubject(final String token) {
		return parseToken(token)
			.getBody()
			.getSubject();
	}

	private ResponseCookie createResponseCookie(final String token, final String prefix, final Long expirationTime) {
		return ResponseCookie.from(prefix, token)
			.maxAge(expirationTime)
			.sameSite("Lax") // 배포시 "None"
			.secure(false) // 배포시 true
			.httpOnly(true)
			.path("/")
			.build();

	}

	private String createToken(final String subject, final Long expirationTime) {
		final Date now = new Date();
		final Date expirationDate = new Date(now.getTime() + expirationTime);

		return Jwts.builder()
			.setSubject(subject)
			.setIssuedAt(now)
			.setExpiration(expirationDate)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	private void validateAccessToken(final String accessToken) {
		try {
			parseToken(accessToken);
		} catch (final ExpiredJwtException e) {
			throw new ExpiredPeriodJwtException(EXPIRED_PERIOD_ACCESS_TOKEN);
		} catch (final JwtException | IllegalArgumentException e) {
			throw new InvalidJwtException(INVALID_ACCESS_TOKEN);
		}

	}

	private void validateRefreshToken(final String refreshToken) {
		try {
			parseToken(refreshToken);
		} catch (final ExpiredJwtException e) {
			throw new ExpiredPeriodJwtException(EXPIRED_PERIOD_REFRESH_TOKEN);
		} catch (final JwtException | IllegalArgumentException e) {
			throw new InvalidJwtException(INVALID_REFRESH_TOKEN);
		}

	}

	private Jws<Claims> parseToken(final String token) {
		return Jwts.parserBuilder()
			.setSigningKey(secretKey)
			.build()
			.parseClaimsJws(token);
	}

}
