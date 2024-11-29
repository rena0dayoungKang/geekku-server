package com.kosta.geekku.config.jwt;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosta.geekku.config.auth.PrincipalDetails;
import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.repository.UserRepository;

public class JwtAuthrizationFilter extends BasicAuthenticationFilter {

	private UserRepository userRepository;
	private CompanyRepository companyRepository;
	private JwtToken jwtToken = new JwtToken();

	public JwtAuthrizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository,CompanyRepository companyRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
		this.companyRepository = companyRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		System.out.println("JwtAuthrizationFilter==============================");
		String uri = request.getRequestURI();
		System.out.println("JwtAuthrizationFilter:" + uri);
		// 1. 로그인 (인증) 이 필요없는 요청은 그대로 진행
//		if (!(uri.contains("/user") || uri.contains("/admin") || uri.contains("/manager") || uri.contains("/company"))) {
		if (!(uri.contains("/user") || uri.contains("/admin") || uri.contains("/company"))) {
			chain.doFilter(request, response);
			return;
		}

		// 2. 토큰을 체크
		String authentication = request.getHeader(JwtProperties.HEADER_STRING);
		if (authentication == null) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 필요함");
			return;
		}

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, String> token = objectMapper.readValue(authentication, Map.class);

		System.out.println("token to Map : " + token);

		// 3. access Token : header로 부터 access Token을 가져와 check
		String accessToken = token.get("access_token");
		if (!accessToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 필요함");
			return;
		}

		// 빈문자열로 바꿈
		accessToken = accessToken.replace(JwtProperties.TOKEN_PREFIX, "");

		// 아이디 유저네임 가져와서 db에 있는지 체크
		try {
			// 1) Access Token
			// 1-1)보안키, 만료시간 check
			String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(accessToken)
					.getClaim("sub").asString();

			String role = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(accessToken)
					.getClaim("role").asString();

			System.out.println("username:" + username);
			System.out.println("role:" + role);

			// 1-2) username check
			if (username == null || username.equals("")) throw new Exception();		
			PrincipalDetails principalDetails = getPrincipayDetails(username, role);
			// 성공했다면
			// 1-3) User를 Authentication로 생성하여 Security Session에 넣어준다. (그러면 Controller에서 사용할
			// 수 있다)
			UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(principalDetails, null,
					principalDetails.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(auth);

			// 컨트롤러에 연동
			chain.doFilter(request, response);
			return;

		} catch (Exception e) {
			e.printStackTrace();
			try {
				// 2) Refresh Token Check : Access Token invalidate일 경우
				// 실패했다면 로그인을 다시 하라고 시도
				String refreshToken = token.get("refresh_token");
				if (!refreshToken.startsWith(JwtProperties.TOKEN_PREFIX)) {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 필요함");
					return;
				}
				refreshToken = refreshToken.replace(JwtProperties.TOKEN_PREFIX, "");

				// 2-1) 보안키, 만료시간 check
				String username = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(refreshToken)
						.getClaim("sub").asString();
				String role = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(refreshToken)
						.getClaim("role").asString();
				System.out.println("username" + username);
				System.out.println("role" + role);
				// 2-2) username check
				if (username == null || username.equals("")) throw new Exception("사용자가 없음"); // 사용자가 DB에 없을때
				PrincipalDetails principalDetails = getPrincipayDetails(username, role);
				
				Optional<User> ouser = userRepository.findByUsername(username);

				// accessToken, refreshToken 다시 만들어 보낸다.
				String reAccessToken = jwtToken.makeAccessToken(username, role);
				String reRefreshToken = jwtToken.makeRefreshToken(username, role);
				
				Map<String, String> map = new HashMap<>();
				map.put("access_token", JwtProperties.TOKEN_PREFIX + reAccessToken);
				map.put("refresh_token", JwtProperties.TOKEN_PREFIX + reRefreshToken);
				String reToken = objectMapper.writeValueAsString(map); // map -> jsonString
				response.addHeader(JwtProperties.HEADER_STRING, reToken);
				response.setContentType("application/json; charset=utf-8");
				response.getWriter().print("token");// 토큰을 다시 줄거야 하는 나만의방법이야

			} catch (Exception e2) {
				e2.printStackTrace();
				// 여기까지 왔다는 것은 1), 2) 둘다 실패했음 -> 다시해라
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인 필요");
			}
		}
	}
	
	PrincipalDetails getPrincipayDetails(String username, String role) throws Exception {
		PrincipalDetails principalDetails =null;
		if(role.equals("user")) { //user
			Optional<User> ouser = userRepository.findByUsername(username);
			if (ouser.isEmpty()) throw new Exception(); // 사용자가 DB에 없을때
			principalDetails = new PrincipalDetails(ouser.get());	
		} else  { //company
			Optional<Company> ocompany = companyRepository.findByUsername(username);
			if (ocompany.isEmpty()) throw new Exception(); // 사용자가 DB에 없을때
			principalDetails = new PrincipalDetails(ocompany.get());
		}
		return principalDetails;
	}

}
