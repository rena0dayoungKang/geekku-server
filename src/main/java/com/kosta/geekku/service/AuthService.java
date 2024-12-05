package com.kosta.geekku.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Transactional
@Service
@RequiredArgsConstructor
public class AuthService {

	@Value("${coolsms.apikey}")
	private String apiKey;

	@Value("${coolsms.apisecret}")
	private String apiSecret;

	@Value("${coolsms.fromnumber}")
	private String fromNumber;

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
		Optional<Auth> authOpt = authRepository.findByEmailAndCertificationNum(email, certificationNum);
		
		if (!authOpt.isPresent()) {
			System.out.println("인증번호 없음");
			return false;
		}
		
		Auth auth = authOpt.get();

		List<User> users = userRepository.findAllByEmail(email);
		if (!users.isEmpty()) {
	        System.out.println("인증번호 일치 - 인증성공 (개인사용자)");
	        authRepository.deleteById(auth.getAuthNum()); // 인증 정보 삭제
	        return true;
		}
		
		List<Company> companies = companyRepository.findAllByEmail(email);
	    if (!companies.isEmpty()) {
	        System.out.println("인증번호 일치 - 인증성공 (기업사용자)");
	        authRepository.deleteById(auth.getAuthNum()); // 인증 정보 삭제
	        return true;
	    }
	    
	    System.out.println("인증 실패");
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

		String resetUrl = "http://localhost:3000/resetPassword?username=" + username + "&email=" + email
				+ "&certificationCode=" + certificationNum;

		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(email);
		message.setSubject("비밀번호 재설정 이메일 발송");
		message.setText("재설정 경로   :   " + resetUrl);

		javaMailSender.send(message);
	}

	public Map<String, Object> resetPwd(AuthDto authDto) throws Exception {
		Auth auth = authRepository.findByEmailAndCertificationNum(authDto.getEmail(), authDto.getCertificationCode())
				.orElseThrow(() -> new Exception("인증이 확인되지 않았습니다."));

		Optional<User> user = userRepository.findByUsernameAndEmail(authDto.getUsername(), authDto.getEmail());
		Optional<Company> company = companyRepository.findByUsernameAndEmail(authDto.getUsername(), authDto.getEmail());
		if (!user.isPresent() && !company.isPresent()) {
			throw new Exception("해당 사용자 정보를 찾을 수 없습니다.");
		}

		Map<String, Object> res = new HashMap<>();
		String encodedPassword = bCryptPasswordEncoder.encode(authDto.getNewPassword());
		if (user.isPresent()) {
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

	// sms 인증코드 생성 및 문자발송
	public boolean sendSms(String phone) throws Exception {
		DefaultMessageService messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret,
				"https://api.coolsms.co.kr");

		int certificationNum = new Random().nextInt(999999);
		String randomCode = String.format("%6d", certificationNum);

		Optional<Auth> existPhone = authRepository.findByPhone(phone);
		if (existPhone.isPresent()) {
			Auth auth = existPhone.get();
			auth.setCertificationNum(certificationNum);
			auth.setExpiresTime(LocalDateTime.now().plusDays(1));
			authRepository.save(auth);
		} else {
			Auth auth = Auth.builder().phone(phone).certificationNum(certificationNum)
					.expiresTime(LocalDateTime.now().plusDays(1)).build();
			authRepository.save(auth);
		}
		
		Message message = new Message();
		message.setFrom(fromNumber);
		message.setTo(phone);
		message.setText("지꾸 인증번호는 " + randomCode + "입니다.");
		SingleMessageSendingRequest singleMessageSendingRequest = new SingleMessageSendingRequest(message);
		
		try {
			SingleMessageSentResponse singleMessageSentResponse = messageService.sendOne(singleMessageSendingRequest);
			return true;
		} catch (Exception exception) {
			System.out.println(exception.getMessage());
			return false;
		}
	}

	// sms 인증 확인
	public boolean verifySms(String phone, int certificationCode) throws Exception {
		Optional<Auth> oVerifyCode = authRepository.findByPhone(phone);
		
		if(!oVerifyCode.isPresent()) {
			System.out.println("인증번호가 일치하지 않음");
			return false;
		}
		
		Auth verifyCode = oVerifyCode.get();
		if (verifyCode.getExpiresTime().isBefore(LocalDateTime.now())) {
			System.out.println("인증 코드 만료");
			return false;
		}
			
	    List<User> users = userRepository.findAllByPhone(phone);
	    if (!users.isEmpty()) {
	        System.out.println("인증번호 일치 - 인증성공 (개인사용자)");
	        authRepository.deleteById(verifyCode.getAuthNum());
	        return true;
	    }

	    List<Company> companies = companyRepository.findAllByPhone(phone);
	    if (!companies.isEmpty()) {
	        System.out.println("인증번호 일치 - 인증성공 (기업사용자)");
	        authRepository.deleteById(verifyCode.getAuthNum()); 
	        return true;
	    }
	    
	    System.out.println("인증 실패");
	    return false;
	} 
}
