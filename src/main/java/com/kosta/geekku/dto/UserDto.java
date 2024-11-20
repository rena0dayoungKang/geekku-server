package com.kosta.geekku.dto;

import com.kosta.geekku.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	
	private String username;// 회원 로그인 아이디
	private String name; // 이름
	private String password;
	private String phone;
	private String email1;
	private String email2;
	private String email;
	private String nickname;
	
	public User toEntity() {
		User user = User.builder()
						.username(username)
						.password(password)
						.name(name)
						.phone(phone)
						.email1(email1)
						.email2(email2)
						.nickname(nickname)
						.profileImage(null)
						.createdAt(null)
						.status(true)
						.type("user")
						.roles("ROLE_USER")
						.build();
		return user;
	}
	
}
