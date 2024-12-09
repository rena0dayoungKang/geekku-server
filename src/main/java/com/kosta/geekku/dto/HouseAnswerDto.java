package com.kosta.geekku.dto;

import java.sql.Timestamp;
import java.util.UUID;

import org.springframework.security.core.userdetails.User;

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
public class HouseAnswerDto {
	private Integer answerHouseNum;
	private String title;
	private String content;
	private Timestamp createdAt;
	
	private UUID companyId;
	private String companyName;
	private String companyProfileImage;
	private String companyPhone;
	private String companyAddress;
	private Integer houseNum;
	private UUID userId;
	private String userName;
	private byte[] userProfileImage;
	private String nickname;
	private String name;
	private Integer viewCount;
	private String address1;
	private String address2;
	private String type;
	
	public HouseAnswer toEntity() {
		return HouseAnswer.builder()
						.answerHouseNum(answerHouseNum)
						.title(title)
						.content(content)
						.createdAt(createdAt)
						.company(Company.builder().companyId(companyId).companyName(companyName).phone(companyPhone).build())
						.house(House.builder().houseNum(houseNum).viewCount(viewCount).address1(address1).address2(address2).type(type).build())
						.build();
	}
}
