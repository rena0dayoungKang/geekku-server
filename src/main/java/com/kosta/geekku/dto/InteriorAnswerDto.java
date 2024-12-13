package com.kosta.geekku.dto;

import java.sql.Timestamp;
import java.util.UUID;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.InteriorAllAnswer;
import com.kosta.geekku.entity.InteriorAllRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteriorAnswerDto {
	private Integer answerAllNum;
	private String content;
	private Timestamp createdAt;

	private UUID companyId;
	private String companyName;
	private String companyProfileImage;
	private String companyPhone;
	private String companyAddress;
	private Integer requestAllNum;
	private UUID userId;
	private String title;
	private Integer viewCount;
	private String address1;
	private String address2;
	private String type;
	private boolean workType;
	private String username;
	private String name;
	private String nickname;
	private byte[] userProfileImage;

	public InteriorAllAnswer toEntity() {
		return InteriorAllAnswer.builder().answerAllNum(answerAllNum).content(content).title(title).createdAt(createdAt)
				.company(Company.builder().companyId(companyId).companyName(companyName).phone(companyPhone).build())
				.interiorAllRequest(InteriorAllRequest.builder().requestAllNum(requestAllNum).build()).build();
	}
}
