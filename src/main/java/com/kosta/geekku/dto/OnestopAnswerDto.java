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
	private Integer viewCount;
	private String address1;
	private String address2;
	private String type;
	private String username;
	private String nickname;
	private String name;
	private byte[] userProfileImage;

	private OnestopDto onestop;


	public OnestopAnswer toEntity() {
		return OnestopAnswer.builder().answerOnestopNum(answerOnestopNum).title(title).content(content)
				.createdAt(createdAt)
				.company(Company.builder().companyId(companyId).companyName(companyName).phone(companyPhone).build())
				.onestop(Onestop.builder().onestopNum(onestopNum).viewCount(viewCount).address1(address1)
						.address2(address2).build())
				.build();
	}
}
