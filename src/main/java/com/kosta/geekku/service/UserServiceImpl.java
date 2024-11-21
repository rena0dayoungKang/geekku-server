package com.kosta.geekku.service;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.geekku.dto.UserDto;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public void joinPerson(UserDto userDto) throws Exception {
		User user = userDto.toEntity();
		
		//회원가입시 기본이미지 설정
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

}
