package com.kosta.geekku.dto;

import java.sql.Date;
import java.sql.Timestamp;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.Estate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstateDto {
	private Integer estateNum;
	private String type; 
	private String address1;
	private String address2;
	private String size1;
	private String size2;
	private Integer roomCount;
	private String rentType;
	private Integer jeonsePrice;
	private Integer monthlyPrice;
	private Integer buyPrice;
	private Integer depositPrice;
	private Integer managePrice;
	private Date availableDate;
	private boolean availableState;
	private Integer totalFloor;
	private Integer floor;
	private Integer bathCount;
	private Integer parking;
	private String utility;
	private String title;
	private String content;
	private Timestamp createdAt;
	private String estateImageNums;
	
	private String companyName;
	private String companyPhone;
	private String companyProfileImage;
	
	public Estate toEntity() {
		Estate estate = Estate.builder()
				.estateNum(estateNum)
				.type(type)
				.address1(address1)
				.address2(address2)
				.size1(size1)
				.size2(size2)
				.roomCount(roomCount)
				.rentType(rentType)
				.availableDate(availableDate)
				.availableState(availableState)
				.totalFloor(totalFloor)
				.floor(floor)
				.bathCount(bathCount)
				.utility(utility)
				.title(title)
				.content(content)
				.createdAt(createdAt)
				.company(Company.builder().companyName(companyName).phone(companyPhone).build())
				.build();
		
		if (rentType.equals("jeonse")) {
			estate.setJeonsePrice(jeonsePrice);
		} else if (rentType.equals("monthly")) {
			estate.setDepositPrice(depositPrice);
			estate.setMonthlyPrice(monthlyPrice);
		} else {
			estate.setBuyPrice(buyPrice);
		}
		
		if (managePrice != null) {
			estate.setManagePrice(managePrice);
		}
		
		if (parking != null) {
			estate.setParking(parking);
		}

		return estate;
	}
}
