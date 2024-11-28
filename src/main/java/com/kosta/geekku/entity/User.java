package com.kosta.geekku.entity;


import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.util.Base64Utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kosta.geekku.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
	// 개인회원
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(columnDefinition = "BINARY(16)", unique = true, nullable = false)
	private UUID userId;
	private String username;// 회원 로그인 아이디
	private String password;
	private String name; // 이름
	private String phone;
	private String email;
	private String nickname;
	@Column(columnDefinition = "MEDIUMBLOB")
	@Lob
	private byte[] profileImage;
	@CreationTimestamp
	private Timestamp createdAt;
	private boolean status; // 회원탈퇴여부 -> 0: 탈퇴X 1: 탈퇴O
	private String type;	//user
	private String fcmToken; // FcmToken 추가

	// OAuth(소셜로그인)를 위해 구성하는 필드
	@Enumerated(EnumType.STRING)
	private Role role;
	private String provider;
	private String providerId;
	@Column(columnDefinition = "MEDIUMBLOB") 
	@Lob
	private byte[] socialProfileImage; // 소셜로그인 프로필이미지

	public UserDto toDto() {		
		UserDto userDto = UserDto.builder()
								.userId(userId.toString())
								.username(username)
								.name(name)
								.phone(phone)
								.email(email)
								.nickname(nickname)
								.password(password)
								.type(type)
								.createdAt(createdAt)
								.build();
		
		if (profileImage != null) {
			try {
				userDto.setProfileImageStr(new String(Base64.encodeBase64(profileImage), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		return userDto;
	}

}
