package com.example.fashionforecastbackend.global.jwt.service;

import static com.example.fashionforecastbackend.global.error.ErrorCode.*;
import static org.springframework.http.HttpHeaders.*;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
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

	//	private static final String EMPTY_SUBJECT = "";
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

	public MemberTokens generateLoginToken(HttpServletResponse response, final String subject, final String role) {
		final String refreshToken = createToken(subject, refreshExpirationTime, role);
		final String accessToken = createToken(subject, accessExpirationTime, role);

		response.addHeader(SET_COOKIE,
			createResponseCookie(refreshToken, REFRESH_COOKIE_PREFIX, refreshExpirationTime).toString());
		response.addHeader(SET_COOKIE,
			createResponseCookie(accessToken, ACCESS_COOKIE_PREFIX, accessExpirationTime).toString());

		return new MemberTokens(accessToken, refreshToken);
	}

	public String generateRefreshToken(HttpServletResponse response, final String subject, final String role) {
		final String refreshToken = createToken(subject, refreshExpirationTime, role);
		response.addHeader(SET_COOKIE,
			createResponseCookie(refreshToken, REFRESH_COOKIE_PREFIX, refreshExpirationTime).toString());
		return refreshToken;
	}

	public String generateAccessToken(final String subject, final String role) {
		return createToken(subject, accessExpirationTime, role);
	}

	//	public void validateTokens(final MemberTokens memberTokens) {
	//		validateRefreshToken(memberTokens.getRefreshToken());
	//		validateAccessToken(memberTokens.getAccessToken());
	//
	//	}

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

	public String getSubject(final String token) {
		return parseToken(token)
			.getBody()
			.getSubject();
	}

	public String getRole(final String token) {
		return parseToken(token)
			.getBody()
			.get("role", String.class);
	}

	private ResponseCookie createResponseCookie(final String token, final String prefix, final Long expirationTime) {
		return ResponseCookie.from(prefix, token)
			.maxAge(expirationTime)
			.sameSite("Lax") // 타 도메인 쿠키 X
			.secure(true) // https 강제
			.httpOnly(true)
			.path("/")
			.build();

	}

	private String createToken(final String subject, final Long expirationTime, final String role) {
		final Date now = new Date();
		final Date expirationDate = new Date(now.getTime() + expirationTime * 1000L);

		return Jwts.builder()
			.setSubject(subject)
			.setIssuedAt(now)
			.setExpiration(expirationDate)
			.claim("role", role)
			.signWith(secretKey, SignatureAlgorithm.HS256)
			.compact();
	}

	public void validateAccessToken(final String accessToken) {
		try {
			parseToken(accessToken);
		} catch (final ExpiredJwtException e) {
			throw new ExpiredPeriodJwtException(EXPIRED_PERIOD_ACCESS_TOKEN);
		} catch (final JwtException | IllegalArgumentException e) {
			throw new InvalidJwtException(INVALID_ACCESS_TOKEN);
		}

	}

	public String validateRefreshToken(final String refreshToken) {
		try {
			Claims claims = parseToken(refreshToken).getBody();
			return claims.getSubject();
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

	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseToken(accessToken).getBody();
		List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(
			claims.get("role").toString()));

		User principal = new User(claims.getSubject(), "", authorities);

		return new UsernamePasswordAuthenticationToken(principal, "", authorities);
	}

}
