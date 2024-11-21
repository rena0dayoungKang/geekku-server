package com.kosta.geekku.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.kosta.geekku.dto.InteriorDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Interior {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer interiorNum;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId")
	private Company company;
	// private UUID companyId; //join column company - companyId

	private boolean possiblePart; // 부분시공 가능 여부-> 0:불가능 1:가능
	private Integer period;
	private Integer recentCount;
	private Integer repairDate;
	private String possibleLocation;
	private Integer coverImage; // -->upload폴더의 num
	private String intro;
	@Column(length = 1000)
	private String content; // 소개글 1000자제한
	@CreationTimestamp
	private Timestamp createdAt;
	
	public InteriorDto toDto() {
		InteriorDto interiorDto = InteriorDto.builder()
				.interiorNum(interiorNum)
				.possiblePart(possiblePart)
				.period(period)
				.recentCount(recentCount)
				.repairDate(repairDate)
				.possibleLocation(possibleLocation)
				.coverImage(coverImage)
				.intro(intro)
				.content(content)
				.createdAt(createdAt)
				.companyName(company.getCompanyName())
				.companyId(company.getCompanyId())
				.build();
		return interiorDto;
	}

}
