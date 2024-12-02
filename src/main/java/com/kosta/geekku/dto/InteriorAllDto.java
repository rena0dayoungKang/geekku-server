package com.kosta.geekku.dto;

import java.sql.Timestamp;

import com.kosta.geekku.entity.InteriorAllRequest;
import com.kosta.geekku.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteriorAllDto {
	private Integer requestAllNum;

	private User user;
	// private UUID userId; // join column User -userId
	private String name;
	private String phone;
	private String type;
	private Integer size;
	private String address1;
	private String address2;
	private Integer money;
	private boolean workType; // 시공종류 -> 0:부분시공 1:전체시공
	private String interiorType;
	private boolean allowPhone; // 연락처 공개 0:비공개 1:공개
	private String title;
	private String addContent;
	private Integer viewCount;
	private Timestamp createAt;

	public InteriorAllRequest toEntity() {
		return InteriorAllRequest.builder().requestAllNum(requestAllNum).user(user).name(name).phone(phone)
				.type(interiorType).size(size).address1(address1).address2(address2).money(money).workType(workType)
				.interiorType(interiorType).allowPhone(allowPhone).title(title).addContent(addContent)
				.createAt(createAt).build();

	}

}
