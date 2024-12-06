package com.kosta.geekku.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;
import com.kosta.geekku.entity.Community;
import com.kosta.geekku.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityDto {
	private Integer communityNum;

	private String type;
	private Integer size;
	private String address1;
	private String address2;
	private String familyType;
	private String interiorType;
	private Date periodStartDate;
	private Date periodEndDate;
	private Integer money;
	private String style;
	private String coverImage;
	private String title;
	private String content;
	private Timestamp createdAt;
	private Integer viewCount;
	private String username;
	private String name;
	private String nickname;
	private String userId;
	private boolean isOwner;
	private byte[] profileImage;
		
	public Community toEntity() {
	    return Community.builder()
	            .type(this.type)
	            .size(this.size)
	            .address1(this.address1)
	            .address2(this.address2)
	            .familyType(this.familyType)
	            .interiorType(this.interiorType)
	            .periodStartDate(this.periodStartDate)
	            .periodEndDate(this.periodEndDate)
	            .money(this.money)
	            .style(this.style)
	            .title(this.title)
	            .content(this.content)
	            .user(User.builder().userId(UUID.fromString(userId)).build())
	            .build();
	}
}
