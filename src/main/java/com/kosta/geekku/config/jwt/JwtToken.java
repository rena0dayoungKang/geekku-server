package com.kosta.geekku.config.jwt;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtToken {
	
	@PostConstruct
	public void init() {
		System.out.println("jwToken bean initailized");
	}
	
	public String makeAccessToken(String username, String role) {
		return JWT.create()
					.withSubject(username)
					.withClaim("role", role)
					.withIssuedAt(new Date(System.currentTimeMillis()))
					.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
					.sign(Algorithm.HMAC512(JwtProperties.SECRET));
	}
	
	public String makeRefreshToken(String username, String role) {
		return JWT.create()
					.withSubject(username)
					.withClaim("role", role)
					.withIssuedAt(new Date(System.currentTimeMillis()))
					.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
					.sign(Algorithm.HMAC512(JwtProperties.SECRET));
	}
	
}
