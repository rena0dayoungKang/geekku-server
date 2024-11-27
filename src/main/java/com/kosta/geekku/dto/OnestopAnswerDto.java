package com.kosta.geekku.dto;

import java.sql.Timestamp;
import java.util.UUID;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.House;
import com.kosta.geekku.entity.HouseAnswer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnestopAnswerDto {
	private Integer answerOnestopNum;
	private String content;
	private Timestamp createdAt;
	
	private UUID companyId;
	private String companyName;
	private String companyProfileImage;
	private String companyPhone;
	private String companyAddress;
	private Integer onestopNum;
	private UUID userId;
	private String title;
}
