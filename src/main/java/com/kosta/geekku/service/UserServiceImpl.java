package com.kosta.geekku.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.geekku.config.jwt.JwtToken;
import com.kosta.geekku.dto.UserDto;
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

	@Override
	public void joinPerson(UserDto userDto) throws Exception {
		User user = userDto.toEntity();

		// 회원가입시 기본이미지 설정
		byte[] defaultProfileImage = Files.readAllBytes(Paths.get("src/main/resources/static/img/profileImg.png"));
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
	public Map<String, String> updateUserInfo(UUID userId, UserDto userDto) throws Exception {
		User user = userRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다"));
		if(userDto.getNickname() != null) user.setNickname(userDto.getNickname());
		if(userDto.getName() != null) user.setName(userDto.getName());
		if(userDto.getPhone() != null) user.setPhone(userDto.getPhone());
		if(userDto.getEmail() != null) user.setEmail(userDto.getEmail());
		userRepository.save(user);
		
		String newAccessToken = jwtToken.makeAccessToken(userDto.getUsername(), userDto.getType());
		String newRefreshToken = jwtToken.makeRefreshToken(userDto.getUsername(), userDto.getType());
		
		Map<String, String> tokens = new HashMap<>();
		tokens.put("accessToken", newAccessToken);
		tokens.put("refreshToken", newRefreshToken);
		return tokens;
	}

	@Override
	public void changePassword(UUID userId, String newPassword) throws Exception {
		User user = userRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다"));
		user.setPassword(newPassword);
		userRepository.save(user);		
	}

	@Override
	public UserDto findIdByPhone(String phone) throws Exception {
		User user = userRepository.findByPhone(phone).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다"));
		return user.toDto();
	}

	@Override
	public UserDto findIdByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다"));
		return user.toDto();
	}

}
