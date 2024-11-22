package com.kosta.geekku.service;

import com.kosta.geekku.dto.UserDto;

public interface UserService {
	void joinPerson(UserDto userDto) throws Exception;
	boolean checkDoubleId(String username) throws Exception;
	boolean checkDoubleNickname(String nickname) throws Exception;
	UserDto login(String username, String password) throws Exception;
}
