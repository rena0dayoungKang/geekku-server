package com.kosta.geekku.dto;

import java.sql.Timestamp;
import java.util.UUID;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.Interior;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteriorDto {
	private Integer interiorNum;
	private String companyName;
	private UUID companyId;
	private boolean possiblePart;
	private Integer period;
	private Integer recentCount;
	private Integer repairDate;
	private String possibleLocation;
	private Integer coverImage;
	private String intro;
	private String content;
	private Timestamp createdAt;
	
	public Interior toEntity() {
		Interior interior = Interior.builder()
					.interiorNum(interiorNum)
					.company(Company.builder().companyId(getCompanyId())
							.companyName(getCompanyName()).build())
					.possiblePart(false)
					.period(period)
					.recentCount(recentCount)
					.repairDate(repairDate)
					.possibleLocation(possibleLocation)
					.coverImage(coverImage)
					.intro(intro)
					.content(content)
					.createdAt(createdAt)
					.build();
		if(createdAt!=null) {
			interior.setCreatedAt(createdAt);
		}
		return interior;
	}
}
