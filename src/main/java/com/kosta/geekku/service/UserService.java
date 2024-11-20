package com.kosta.geekku.service;

import com.kosta.geekku.dto.UserDto;

public interface UserService {
	void joinPerson(UserDto userDto) throws Exception;
}
