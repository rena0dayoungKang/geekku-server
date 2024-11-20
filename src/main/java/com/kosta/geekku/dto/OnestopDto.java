package com.kosta.geekku.dto;

import java.sql.Timestamp;

import com.kosta.geekku.entity.Onestop;
import com.kosta.geekku.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OnestopDto {
	// 한번에 꾸하기

	private Integer onestopNum;
	private User user;
	private String type;
	private String address1;
	private String address2;
	private String rentType;
	private Integer size;
	private Integer money;
	private String workType;
	private String interiorType;
	private Integer movePersons;
	private Integer allowPhone;
	private String title;
	private String content;
	private Integer viewCount;
	private Timestamp createdAt;

	public Onestop toEntity() {
		return Onestop.builder().title(title).content(content).build();

	}
}
