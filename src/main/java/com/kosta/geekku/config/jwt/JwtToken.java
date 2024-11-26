package com.kosta.geekku.config.jwt;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtToken {
	
	@PostConstruct
	public void init() {
		System.out.println("jwToken bean initailized");
		//System.out.println("secret key : " + JwtProperties.SECRET);
	}
	
	public String makeAccessToken(String username, String type) {
		return JWT.create()
					.withSubject(username)
					.withClaim("type", type)
					.withIssuedAt(new Date(System.currentTimeMillis()))
					.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
					.sign(Algorithm.HMAC512(JwtProperties.SECRET));
	}
	
	public String makeRefreshToken(String username, String type) {
		return JWT.create()
					.withSubject(username)
					.withClaim("type", type)
					.withIssuedAt(new Date(System.currentTimeMillis()))
					.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
					.sign(Algorithm.HMAC512(JwtProperties.SECRET));
	}
	
}
