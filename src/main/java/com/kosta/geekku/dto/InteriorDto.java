package com.kosta.geekku.dto;

import java.sql.Timestamp;
import java.util.UUID;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.Interior;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteriorDto {
	private Integer interiorNum;
	private String companyName;
	private UUID companyId;
	private boolean possiblePart;
	private Float period;
	private Integer recentCount;
	private Float repairDate;
	private String possibleLocation;
	private byte[] coverImage;
	private String coverImageStr;
	private String intro;
	private String content;
	private Timestamp createdAt;
	private boolean regStatus;

	public Interior toEntity() {
		Interior interior = Interior.builder().interiorNum(interiorNum)
//				.company(Company.builder().companyId(getCompanyId()).companyName(getCompanyName()).build())
				.company(Company.builder().companyName(getCompanyName()).build()).possiblePart(possiblePart)
				.period(period).recentCount(recentCount).repairDate(repairDate).possibleLocation(possibleLocation)
				.possiblePart(possiblePart).coverImage(coverImage).intro(intro).content(content).createdAt(createdAt)
				.build();
		if (createdAt != null) {
			interior.setCreatedAt(createdAt);
		}
		return interior;
	}

	@Override
	public String toString() {
		return "InteriorDto [interiorNum=" + interiorNum + ", companyName=" + companyName + ", companyId=" + companyId
				+ ", possiblePart=" + possiblePart + ", period=" + period + ", recentCount=" + recentCount
				+ ", repairDate=" + repairDate + ", possibleLocation=" + possibleLocation + ", intro=" + intro
				+ ", content=" + content + ", createdAt=" + createdAt + ", regStatus=" + regStatus + "]";
	}

}
