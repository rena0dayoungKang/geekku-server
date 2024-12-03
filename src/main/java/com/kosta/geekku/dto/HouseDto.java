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
	private Integer depositPrice;
	private Integer buyPrice;
	private Date requestDate;
	private boolean requestState;
	private boolean allowPhone;
	private String title;
	private String content;
	private Integer viewCount;
	private Timestamp createdAt;
	
	private UUID userId;
	private String nickname;
	private String name;
	private String userProfileImage;
	private String userPhone;
	
	public House toEntity() {
		House house = House.builder()
				.houseNum(houseNum)
				.type(type)
				.address1(address1)
				.address2(address2)
				.rentType(rentType)
				.size(size)
				.requestDate(requestDate)
				.requestState(requestState)
				.allowPhone(allowPhone)
				.title(title)
				.content(content)
				.viewCount(viewCount)
				.createdAt(createdAt)
				.user(User.builder().userId(userId).name(name).nickname(nickname).phone(userPhone).build())
				.build();
		
		if (rentType.equals("jeonse")) {
			house.setJeonsePrice(jeonsePrice);
		} else if (rentType.equals("monthly")) {
			house.setMonthlyPrice(monthlyPrice);
			house.setDepositPrice(depositPrice);
		} else {
			house.setBuyPrice(buyPrice);
		}
		
		return house;
	}
}
