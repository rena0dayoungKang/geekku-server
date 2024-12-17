package com.kosta.geekku.config.oauth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosta.geekku.config.auth.PrincipalDetails;
import com.kosta.geekku.config.jwt.JwtProperties;
import com.kosta.geekku.config.jwt.JwtToken;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

//	private static final String URI = "http://localhost:3000/login";
	private static final String URI = "https://www.geekku.store/login";
	private JwtToken jwtToken = new JwtToken();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		PrincipalDetails principalDetails =(PrincipalDetails)authentication.getPrincipal();
		String accessToken = jwtToken.makeAccessToken(principalDetails.getUsername(), "ROLE_USER");
		String refreshToken = jwtToken.makeRefreshToken(principalDetails.getUsername(), "ROLE_USER");

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> map = new HashMap<>();
		map.put("access_token", JwtProperties.TOKEN_PREFIX + accessToken);
		map.put("refresh_token", JwtProperties.TOKEN_PREFIX + refreshToken);

		String token = objectMapper.writeValueAsString(map);
		//System.out.println("OAuth2SuccessHandler:"+token);
		 
		String redirectUrl = UriComponentsBuilder.fromUriString(URI)
												.queryParam("token", token)
												.build().toUriString();
		//System.out.println("OAuth2SuccessHandler - Redirect URL: " + redirectUrl);
		response.sendRedirect(redirectUrl);
	}

}
