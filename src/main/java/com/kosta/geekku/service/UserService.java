package com.kosta.geekku.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.UserDto;

public interface UserService {
	void joinPerson(UserDto userDto) throws Exception;

	boolean checkDoubleId(String username) throws Exception;

	boolean checkDoubleNickname(String nickname) throws Exception;

	UserDto login(String username, String password) throws Exception;

	UserDto getUser(UUID userId) throws Exception;

	UserDto getUser(String username) throws Exception;

	Map<String, Object> updateUserInfo(UUID userId, UserDto userDto, MultipartFile file) throws Exception;

	Map<String, Object> changePassword(UUID userId, String newPassword) throws Exception;

	UserDto findIdByPhone(String phone) throws Exception;

	UserDto findIdByEmail(String email) throws Exception;
}
