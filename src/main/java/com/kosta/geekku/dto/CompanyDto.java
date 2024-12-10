package com.kosta.geekku.dto;

import java.sql.Timestamp;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompanyDto {
	
	private String companyId;
	private String username;
	private String type;
	private String password;
	private String phone;
	private String email;
	
	private byte[] profileImage;
	private String profileImageStr;
	
	private String companyNumber;
	private String ceoName;
	private String companyName;
	private String companyAddress;
	private Integer certificationImageNum;
	private String certificationImagePath;
	private Timestamp createdAt;
	private boolean regStatus;

	//부동산일경우
	private String estateNumber; 
	
	private Role role;
	
	public Company toEntity() {		
		Company company = Company.builder()
								.username(username)
								.type(type)
								.password(password)
								.phone(phone)
								.email(email)
								.status(false)
								.role(role)
								.type(type)
								.profileImage(profileImage)
								.companyNumber(companyNumber)
								.ceoName(ceoName)
								.companyName(companyName)
								.companyAddress(companyAddress)
								.estateNumber(estateNumber)
								.regStatus(regStatus)
								.createdAt(createdAt)
								.build();
		return company;
	}
	
}
