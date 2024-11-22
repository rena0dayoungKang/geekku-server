package com.kosta.geekku.config.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosta.geekku.config.auth.PrincipalDetails;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	// 로그인 성공 시 Access Token 과 Refresh Token생성, 클라이언트에 반환

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}

	private JwtToken jwtToken = new JwtToken();

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		System.out.println("JwtAuthenticationFilter =============================================");
		PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();
		
		String accesToken = jwtToken.makeAccessToken(principalDetails.getUsername());
		String refreshToken = jwtToken.makeRefreshToken(principalDetails.getUsername());
		
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		map.put("access_token", JwtProperties.TOKEN_PREFIX+accesToken);
		map.put("refresh_token", JwtProperties.TOKEN_PREFIX+refreshToken);
		
		String token = objectMapper.writeValueAsString(map);
		System.out.println(token);
		
		response.addHeader(JwtProperties.HEADER_STRING, token);
		response.setContentType("application/json; charset=utf-8");
		response.getWriter().write("true");
		
	}

}
