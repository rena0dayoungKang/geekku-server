package com.kosta.geekku.dto;

import java.sql.Timestamp;
import java.util.UUID;

import com.kosta.geekku.entity.Interior;
import com.kosta.geekku.entity.InteriorRequest;
import com.kosta.geekku.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteriorRequestDto {
	private Integer requestNum;
	private UUID userId;
	private Integer interiorNum;
	private String period; 
	private String type;
	private String status;
	private String phone;
	private String size;
	private String name;
	private String nickname;
	private byte[] profileImage;
	private String allowTime;
	private String content;
	private Timestamp createdAt;
	private UUID companyId;
	
	public InteriorRequest toEntity() {
		InteriorRequest request = InteriorRequest.builder()
				.requestNum(requestNum)
				.user(User.builder().userId(userId).name(name).nickname(nickname).build())
				.interior(Interior.builder().interiorNum(interiorNum).build())
				.period(period)
				.type(type)
				.status(status)
				.phone(phone)
				.size(size)
				.allowTime(allowTime)
				.content(content)
				.createdAt(createdAt)
				.build();
		return request;
	}
}
