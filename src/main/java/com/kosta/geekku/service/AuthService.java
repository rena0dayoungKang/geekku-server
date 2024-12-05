package com.kosta.geekku.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

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

	public void sendVerificationCode(String email) {
		int certificationNum = (int) (Math.random() * 900000) + 100000;

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

	public boolean verifyCode(String email, int certificationNum) {
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
}
