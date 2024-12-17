package com.kosta.geekku.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.kosta.geekku.config.jwt.JwtAuthenticationFilter;
import com.kosta.geekku.config.jwt.JwtAuthrizationFilter;
import com.kosta.geekku.config.oauth.OAuth2SuccessHandler;
import com.kosta.geekku.config.oauth.PrincipalOAuth2UserService;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.repository.UserRepository;

@Configuration
@EnableWebSecurity
public class WebSecuritySconfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CorsFilter corsFilter;

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private OAuth2SuccessHandler oAuth2SuccessHandler;

	@Autowired
	private PrincipalOAuth2UserService principalOAuth2UserService;

	@Bean
	public BCryptPasswordEncoder encoderPassword() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilter(corsFilter)
				.csrf().disable()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.formLogin().disable()
			.httpBasic().disable()
			.addFilterAt(new JwtAuthenticationFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);

		// OAuth2 Login
		http.oauth2Login()
				.authorizationEndpoint().baseUri("/oauth2/authorization")
				.and()
				.redirectionEndpoint().baseUri("/login/oauth2/code/*")
				.and()
				.userInfoEndpoint().userService(principalOAuth2UserService)
				.and()
				.successHandler(oAuth2SuccessHandler)
				.failureHandler(oAuth2AuthenticationFailureHandler());

		http.addFilter(new JwtAuthrizationFilter(authenticationManager(), userRepository,companyRepository))
				.authorizeRequests()
				.antMatchers("/resources/**").permitAll() //정적 리소스는 예외
				.antMatchers("/user/**").access("hasRole('ROLE_USER')") //hasRole("ROLE_USER")
				.antMatchers("/company/**").access("hasRole('ROLE_COMPANY')") //.hasRole("ROLE_COMPANY")
				.antMatchers("/mypage/**").authenticated()
				.anyRequest().permitAll();
	}
	
	private AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
//		return new SimpleUrlAuthenticationFailureHandler("http://localhost:3000/");
		return new SimpleUrlAuthenticationFailureHandler("https://geekku.store/");
	}
}
