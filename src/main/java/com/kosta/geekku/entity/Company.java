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

import com.kosta.geekku.dto.CompanyDto;

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
	private String type; // estate, interior
	private String password;
	private String phone;
	private String email;
	@Column(columnDefinition = "MEDIUMBLOB")
	@Lob
	private byte[] profileImage;
	private String companyNumber;// 사업자 등록번호
	private String ceoName;// 대표자명
	private String companyName;
	private String companyAddress;
	private String estateNumber; //중개등록번호
	@Column(columnDefinition = "MEDIUMBLOB")
	@Lob
	private byte[] companyCertificationImage;// 사업자등록증 이미지
	@CreationTimestamp
	private Timestamp createdAt;
	private boolean status;// 회원탈퇴여부 -> 0: 탈퇴X 1: 탈퇴O
	
	@Enumerated(EnumType.STRING)
	private Role role;	//user, company 구분
	
	private String fcmToken; // FcmToken 추가
	
	public CompanyDto toDto() {		
		CompanyDto companyDto = CompanyDto.builder()
										.companyId(companyId.toString())
										.username(username)
										.password(password)
										.type(type)
										.phone(phone)
										.email(email)
										.ceoName(ceoName)
										.companyNumber(companyNumber)
										.companyName(companyName)
										.companyAddress(companyAddress)
										.estateNumber(estateNumber)
										.createdAt(createdAt)
										.role(role)
										.build();
		if (profileImage != null) {
			try {
				companyDto.setProfileImageStr(new String(Base64.encodeBase64(profileImage), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return companyDto;							
	}
}
