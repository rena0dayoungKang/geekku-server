package com.kosta.geekku.config.jwt;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtPropertiesImpl implements JwtProperties {
	
	@Value("${jwt.secret}")
	private String secretKey;
	
	@PostConstruct
	public void checkSecret() {
		System.out.println("JwtPropertiesImpl의  secretKey : " + secretKey);
	}
	
//	@Override
//	public String getSecret() {
//		return secretKey;
//	}

}
