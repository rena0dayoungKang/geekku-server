package com.kosta.geekku.entity;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Company {
	// 기업회원
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(columnDefinition = "BINARY(16)", unique = true, nullable = false)	
	private UUID companyId;

	private String username; // 기업회원 로그인 아이디
	private String type; // estate:부동산, interior:인테리어 사업자 구분
	private String password;
	private String phone;
	private String email1;
	private String email2;// @naver.com
	@Column(columnDefinition = "MEDIUMBLOB")
	@Lob
	private byte[] profileImage;
	private String companyNumber;// 사업자 등록번호
	private String address;
	private String ceoName;// 대표자명
	private String companyName;
	private String companyAddress;
	private String estateNumber; //중개등록번호
	private String companyCertificationImage;// 사업자등록증 이미지
	@CreationTimestamp
	private Timestamp createdAt;
	private boolean status;// 회원탈퇴여부 -> 0: 탈퇴X 1: 탈퇴O

}
