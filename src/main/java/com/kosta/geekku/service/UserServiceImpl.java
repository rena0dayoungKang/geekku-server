package com.kosta.geekku.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosta.geekku.config.jwt.JwtProperties;
import com.kosta.geekku.config.jwt.JwtToken;
import com.kosta.geekku.dto.UserDto;
import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private JwtToken jwtToken;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void joinPerson(UserDto userDto) throws Exception {
		User user = userDto.toEntity();

		// 회원가입시 기본이미지 설정 (클래스패스 리소스 로딩 방식 적용)
		ClassPathResource resource = new ClassPathResource("static/img/profileImg.png");
		//byte[] defaultProfileImage = Files.readAllBytes(Paths.get("src/main/resources/static/img/profileImg.png"));
		byte[] defaultProfileImage;
		try (InputStream in = resource.getInputStream()) {
			defaultProfileImage = in.readAllBytes();
		}
		user.setProfileImage(defaultProfileImage);

		userRepository.save(user);
	}

	@Override
	public boolean checkDoubleId(String username) throws Exception {
		boolean isUser = userRepository.findByUsername(username).isPresent();
		boolean isCompany = companyRepository.findByUsername(username).isPresent();
		return (isUser || isCompany);
	}

	@Override
	public boolean checkDoubleNickname(String nickname) throws Exception {
		return userRepository.findByNickname(nickname).isPresent();
	}

	@Override
	public UserDto login(String username, String password) throws Exception {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new Exception("아이디 오류"));
		if (!user.getPassword().equals(password)) {
			throw new Exception("비밀번호 오류");
		}
		return user.toDto();
	}

	@Override
	public UserDto getUser(UUID userId) throws Exception {
		User user = userRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다"));
		return user.toDto();
	}

	@Override
	public UserDto getUser(String username) throws Exception {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다"));
		return user.toDto();
	}

	@Override
	public Map<String, Object> updateUserInfo(UUID userId, UserDto userDto, MultipartFile profile) throws Exception {
		User user = userRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다"));

		if (userDto.getNickname() != null)
			user.setNickname(userDto.getNickname());
		if (userDto.getName() != null)
			user.setName(userDto.getName());
		if (userDto.getPhone() != null)
			user.setPhone(userDto.getPhone());
		if (userDto.getEmail() != null)
			user.setEmail(userDto.getEmail());

		if (profile != null && !profile.isEmpty()) {
			user.setProfileImage(profile.getBytes());
		}

		userRepository.save(user);

		String newAccessToken = jwtToken.makeAccessToken(user.getUsername(), user.getRole().toString());
		String newRefreshToken = jwtToken.makeRefreshToken(user.getUsername(), user.getRole().toString());

		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", JwtProperties.TOKEN_PREFIX + newAccessToken);
		tokens.put("refresh_token", JwtProperties.TOKEN_PREFIX + newRefreshToken);

		Map<String, Object> res = new HashMap<>();
		res.put("token", objectMapper.writeValueAsString(tokens));
		res.put("user", user.toDto());
		return res;
	}

	@Override
	public Map<String, Object> changePassword(UUID userId, String newPassword) throws Exception {
		User user = userRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다"));
		user.setPassword(newPassword);
		userRepository.save(user);

		String newAccessToken = jwtToken.makeAccessToken(user.getUsername(), user.getRole().toString());
		String newRefreshToken = jwtToken.makeRefreshToken(user.getUsername(), user.getRole().toString());
		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", JwtProperties.TOKEN_PREFIX + newAccessToken);
		tokens.put("refresh_token", JwtProperties.TOKEN_PREFIX + newRefreshToken);

		Map<String, Object> res = new HashMap<>();
		res.put("token", objectMapper.writeValueAsString(tokens));
		res.put("user", user.toDto());

		return res;
	}

	@Override
	public List<UserDto> findIdByEmail(String email) throws Exception {
		List<UserDto> userList = userRepository.findAllByEmail(email).stream().map(e -> e.toDto())
				.collect(Collectors.toList());
		return userList;
	}

	@Override
	public List<UserDto> findIdByPhone(String phone) throws Exception {
		List<UserDto> userList = userRepository.findAllByPhone(phone).stream().map(e -> e.toDto())
				.collect(Collectors.toList());
		return userList;
	}

	@Override
	public void logout(UUID id) throws Exception {
		Optional<User> ouser = userRepository.findById(id);
		if(ouser.isPresent()) {
			User user = ouser.get();
			user.setFcmToken("");
			userRepository.save(user);
		} else {
			Optional<Company> ocompany = companyRepository.findById(id);
			if(ocompany.isPresent()) {
				Company company = ocompany.get();
				company.setFcmToken("");
				companyRepository.save(company);
			}
		}
	}
}
