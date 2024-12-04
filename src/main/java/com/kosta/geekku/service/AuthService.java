package com.kosta.geekku.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosta.geekku.config.jwt.JwtProperties;
import com.kosta.geekku.config.jwt.JwtToken;
import com.kosta.geekku.dto.AuthDto;
import com.kosta.geekku.entity.Auth;
import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.AuthRepository;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final JavaMailSender javaMailSender;
	private final AuthRepository authRepository;
	private final UserRepository userRepository;
	private final CompanyRepository companyRepository;
	private final JwtToken jwtToken;
	private final ObjectMapper objectMapper;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public void sendVerificationCode(String email) throws Exception {
		int certificationNum = new Random().nextInt(999999);

		Optional<Auth> existEmail = authRepository.findByEmail(email);

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("인증번호 발송");
		message.setText("인증번호 : " + certificationNum);

		javaMailSender.send(message);

		if (existEmail.isPresent()) {
			Auth auth = existEmail.get();
			auth.setCertificationNum(certificationNum);
			authRepository.save(auth);
		} else {
			Auth auth = Auth.builder().email(email).certificationNum(certificationNum).build();
			authRepository.save(auth);
		}
	}

	public boolean verifyCode(String email, int certificationNum) throws Exception {
		Map<String, Object> map = new HashMap<>();

		Optional<Auth> auth = authRepository.findByEmailAndCertificationNum(email, certificationNum);
		if (!auth.isPresent()) {
			System.out.println("인증번호 없음");
			return false;
		}

		Optional<User> user = userRepository.findByEmail(email);
		if (user.isPresent()) {
			if (auth.get().getEmail().equals(user.get().getEmail())) {
				System.out.println("인증번호 일치 - 인증성공 (개인사용자)");
				return true;
			}
		}

		Optional<Company> company = companyRepository.findByEmail(email);
		if (company.isPresent()) {
			if (auth.get().getEmail().equals(company.get().getEmail())) {
				System.out.println("인증번호 일치 - 인증성공 (기업사용자)");
				return true;
			}
		}
		System.out.println(map);
		return false;
	}

	public void sendPwdResetUrl(String username, String email) throws Exception {
		int certificationNum = new Random().nextInt(999999);
		
		Optional<User> user = userRepository.findByUsernameAndEmail(username, email);
		Optional<Company> company = companyRepository.findByUsernameAndEmail(username, email);
		if (!user.isPresent() && !company.isPresent()) {
			System.out.println("해당 아이디와 이메일로 가입된 사용자가 없습니다.");
			return;
		}
		
		Optional<Auth> existEmail = authRepository.findByEmail(email);
		if (existEmail.isPresent()) {
			Auth auth = existEmail.get();
			auth.setCertificationNum(certificationNum);
			authRepository.save(auth);
		} else {
			Auth auth = Auth.builder().email(email).certificationNum(certificationNum).build();
			authRepository.save(auth);
		}
		
		String resetUrl = "http://localhost:3000/resetPassword?username=" + username + "&email=" + email + "&certificationCode=" + certificationNum;
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("비밀번호 재설정 이메일 발송");
		message.setText("재설정 경로 : " + resetUrl);
		
		javaMailSender.send(message);
	}
	
	public Map<String, Object> resetPwd(AuthDto authDto) throws Exception {
		Auth auth = authRepository.findByEmailAndCertificationNum(authDto.getEmail(), authDto.getCertificationCode()).orElseThrow(() -> new Exception("인증이 확인되지 않았습니다."));
		
		Optional<User> user = userRepository.findByUsernameAndEmail(authDto.getUsername(), authDto.getEmail());
		Optional<Company> company = companyRepository.findByUsernameAndEmail(authDto.getUsername(), authDto.getEmail());
		if (!user.isPresent() && !company.isPresent()) {
			throw new Exception("해당 사용자 정보를 찾을 수 없습니다.");
		}
		
		Map<String, Object> res = new HashMap<>();
		String encodedPassword = bCryptPasswordEncoder.encode(authDto.getNewPassword());
		if(user.isPresent()) {
			User nUser = user.get();
			nUser.setPassword(encodedPassword);
			userRepository.save(nUser);
			
			String newAccessToken = jwtToken.makeAccessToken(nUser.getUsername(), nUser.getRole().toString());
			String newRefreshToken = jwtToken.makeRefreshToken(nUser.getUsername(), nUser.getRole().toString());
			Map<String, String> tokens = new HashMap<>();
			tokens.put("access_token", JwtProperties.TOKEN_PREFIX + newAccessToken);
			tokens.put("refresh_token", JwtProperties.TOKEN_PREFIX + newRefreshToken);
			
			res.put("tokens", objectMapper.writeValueAsString(res));
			res.put("user", nUser.toDto());
			
		} else if (company.isPresent()) {
			Company nCompany = company.get();
			nCompany.setPassword(encodedPassword);
			companyRepository.save(nCompany);
			
			String newAccessToken = jwtToken.makeAccessToken(nCompany.getUsername(), nCompany.getRole().toString());
			String newRefreshToken = jwtToken.makeRefreshToken(nCompany.getUsername(), nCompany.getRole().toString());
			Map<String, String> tokens = new HashMap<>();
			tokens.put("access_token", JwtProperties.TOKEN_PREFIX + newAccessToken);
			tokens.put("refresh_token", JwtProperties.TOKEN_PREFIX + newRefreshToken);

			res.put("tokens", objectMapper.writeValueAsString(res));
			res.put("company", nCompany.toDto());			
		}
		return res;
	}
}
