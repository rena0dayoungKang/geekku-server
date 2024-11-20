package com.kosta.geekku.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kosta.geekku.dto.UserDto;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public void joinPerson(UserDto userDto) throws Exception {
		User user = userDto.toEntity();
		userRepository.save(user);
	}

}
