package com.kosta.geekku.dto;

import java.sql.Timestamp;
import java.util.UUID;

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
	// �븳踰덉뿉 袁명븯湲�

	private Integer onestopNum;
	private User user;
	private String type;
	private String address1;
	private String address2;
	private String rentType;
	private Integer size;
	private Integer money;
	private boolean workType;// 시공종류 -> 0:부분시공 1:전체시공
	private String interiorType;
	private Integer movePersons;
	private boolean allowPhone; // 연락처 공개 0:비공개 1:공개
	private String title;
	private String content;
	private Integer viewCount;
	private Timestamp createdAt;
	
	private UUID userId;
	private String nickname;
	private String userProfileImage;
	private String userPhone;
	

	public Onestop toEntity() {
		return Onestop.builder().onestopNum(onestopNum).user(user).type(type).address1(address1).address2(address2)
				.rentType(rentType).size(size).money(money).workType(workType).interiorType(interiorType)
				.movePersons(movePersons).allowPhone(allowPhone).title(title).content(content).viewCount(viewCount)
				.user(User.builder().userId(userId).nickname(nickname).phone(userPhone).build())
				.createdAt(createdAt).build();
	}
}
