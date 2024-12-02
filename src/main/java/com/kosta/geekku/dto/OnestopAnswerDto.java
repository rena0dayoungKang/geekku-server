package com.kosta.geekku.dto;

import java.sql.Timestamp;
import java.util.UUID;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.Onestop;
import com.kosta.geekku.entity.OnestopAnswer;

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

	public OnestopAnswer toEntity() {
		return OnestopAnswer.builder().answerOnestopNum(answerOnestopNum).content(content).createdAt(createdAt)
				.company(Company.builder().companyId(companyId).companyName(companyName).phone(companyPhone).build())
				.onestop(Onestop.builder().onestopNum(onestopNum).build()).build();
	}
}
