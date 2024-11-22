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
		System.out.println("secret key : " + JwtProperties.SECRET);
	}
	
	@Autowired
	private JwtProperties jwtProperties;
	
	public void test() {
		System.out.println("JwtProperties Instance: " + jwtProperties.getClass().getName());
	    System.out.println("Secret Key: " + jwtProperties.SECRET);
	}
	
	public String makeAccessToken(String username) {
		return JWT.create()
					.withSubject(username)
					.withIssuedAt(new Date(System.currentTimeMillis()))
					.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.ACCESS_EXPIRATION_TIME))
					.sign(Algorithm.HMAC512(jwtProperties.SECRET));
	}
	
	public String makeRefreshToken(String username) {
		return JWT.create()
					.withSubject(username)
					.withIssuedAt(new Date(System.currentTimeMillis()))
					.withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
					.sign(Algorithm.HMAC512(jwtProperties.SECRET));
	}
	
}
