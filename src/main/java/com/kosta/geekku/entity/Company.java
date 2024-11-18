package com.kosta.geekku.entity;

import java.sql.Timestamp;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

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
public class Company {
	// 기업회원
	@Id
	private UUID companyId;
	private String username; // 기업회원 로그인 아이디
	private String type; // estate:부동산, interior:인테리어 사업자 구분
	private String password;
	private String phone;
	private String email1;
	private String email2;// @naver.com
	private String profileImage;
	private String companyNumber;// 사업자 등록번호
	private String ceoName;// 대표자명
	private String companyName;
	private String companyAddress;
	private String estateNumber;
	private String companyCertificationImage;// 사업자등록증 이미지
	@CreationTimestamp
	private Timestamp createdAt;
	private boolean status;// 회원탈퇴여부 -> 0: 탈퇴X 1: 탈퇴O

}
