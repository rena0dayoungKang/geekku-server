package com.kosta.geekku.config.jwt;

public interface JwtProperties {
	String SECRET = "코스타";
	String HEADER_STRING = "Authorization";
	String TOKEN_PREFIX = "Bearer ";

	Integer ACCESS_EXPIRATION_TIME = 60000 * 60 * 1; // 1시간
	Integer REFRESH_EXPIRATION_TIME = 60000 * 60 * 24 * 7; // 7일
	
//	String getSecret();	//JWT비밀키
}
