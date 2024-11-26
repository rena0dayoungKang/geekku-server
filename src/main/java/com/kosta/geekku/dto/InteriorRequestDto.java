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
	private Integer period; 
	private Integer type;
	private Integer status; 
	private Integer size;
	private String name;	
	private Integer allowTime;
	private String content;
	private Timestamp createdAt;
	
	public InteriorRequest toEntity() {
		InteriorRequest request = InteriorRequest.builder()
				.requestNum(requestNum)
				.user(User.builder().userId(userId).build())
				.interior(Interior.builder().interiorNum(interiorNum).build())
				.period(period)
				.type(type)
				.status(status)
				.size(size)
				.name(name)
				.allowTime(allowTime)
				.content(content)
				.createdAt(createdAt)
				.build();
		return request;
	}
}
