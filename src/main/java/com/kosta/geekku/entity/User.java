package com.kosta.geekku.entity;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
	// 개인회원
	@Id
	private UUID userId;
	private String username;// 회원 로그인 아이디
	private String name; // 이름
	private String phone;
	private String email1;
	private String email2; // @naver.com
	private String nickname;
	private byte[] profileImage;
	@CreationTimestamp
	private Timestamp createdAt;
	private boolean status; // 회원탈퇴여부 -> 0: 탈퇴X 1: 탈퇴O
	private String type;

	// OAuth(소셜로그인)를 위해 구성하는 필드
	private String roles;
	private String provider;
	private String providerId;
	@Column(columnDefinition = "MEDIUMBLOB")
	@Lob
	private byte[] socialProfileImage; // 소셜로그인 프로필이미지

}
