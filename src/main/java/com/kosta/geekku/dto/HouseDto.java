package com.kosta.geekku.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import com.kosta.geekku.entity.House;
import com.kosta.geekku.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HouseDto {
	private Integer houseNum;
	private String type;
	private String address1;
	private String address2;
	private String rentType; 
	private Integer size;
	private Integer jeonsePrice;
	private Integer monthlyPrice;
	private Integer buyPrice;
	private Date requestDate;
	private boolean allowPhone;
	private String title;
	private String content;
	private Integer viewCount;
	private Timestamp createdAt;
	
	private UUID userId;
	private String name;
	private String userProfileImage;
	private String userPhone;
	
	public House toEntity() {
		House house = House.builder()
				.houseNum(houseNum)
				.type(type)
				.address1(address1)
				.address2(address2)
				.size(size)
				.requestDate(requestDate)
				.allowPhone(allowPhone)
				.title(title)
				.content(content)
				.viewCount(viewCount)
				.createdAt(createdAt)
				.user(User.builder().userId(userId).build())
				.build();
		
		if (rentType.equals("jeonse")) {
			house.setJeonsePrice(jeonsePrice);
		} else if (rentType.equals("monthly")) {
			house.setMonthlyPrice(monthlyPrice);
		} else {
			house.setBuyPrice(buyPrice);
		}
		
		return house;
	}
}
