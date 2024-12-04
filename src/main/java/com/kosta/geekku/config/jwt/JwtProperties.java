package com.kosta.geekku.config.jwt;

public interface JwtProperties {
	public String SECRET = "코스타";
	public String HEADER_STRING = "Authorization";
	public String TOKEN_PREFIX = "Bearer ";

	public Integer ACCESS_EXPIRATION_TIME = 60000 * 60 * 1; // 1시간
	public Integer REFRESH_EXPIRATION_TIME = 60000 * 60 * 24 * 1; // 7일
}
