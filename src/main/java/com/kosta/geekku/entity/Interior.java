package com.kosta.geekku.entity;

import java.sql.Timestamp;
import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.kosta.geekku.dto.InteriorDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
//@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Interior {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer interiorNum;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId")
	private Company company;
	// private UUID companyId; //join column company - companyId

	private boolean possiblePart; // 부분시공 가능 여부-> 0:불가능 1:가능
	private Float period;
	private Integer recentCount;
	private Float repairDate;
	private String possibleLocation;
	@Column(columnDefinition = "MEDIUMBLOB")
	@Lob
	private byte[] coverImage; 
	private String intro;
	@Column(length = 1000)
	private String content; // 소개글 1000자제한
	@CreationTimestamp
	private Timestamp createdAt;

	public InteriorDto toDto() {
		InteriorDto interiorDto = InteriorDto.builder().interiorNum(interiorNum)
				.possiblePart(possiblePart)
				.period(period)
				.recentCount(recentCount)
				.repairDate(repairDate)
				.possibleLocation(possibleLocation)
				.possiblePart(possiblePart)
				.coverImage(coverImage)
				.intro(intro)
				.content(content)
				.createdAt(createdAt)
				.companyName(company.getCompanyName())
				.companyId(company.getCompanyId())
				.regStatus(company.isRegStatus())
				.build();
		
		if(coverImage != null) {
			try {
			    String encodedImage = Base64.getEncoder().encodeToString(coverImage); // Base64 인코딩
			    interiorDto.setCoverImageStr(encodedImage);
			} catch(Exception e) {
				e.printStackTrace();
				throw new RuntimeException("이미지 인코딩 중 오류 발생", e);
			}
		}
		
		return interiorDto;
	}

	@Override
	public String toString() {
		return "Interior [interiorNum=" + interiorNum + ", company=" + company + ", possiblePart=" + possiblePart
				+ ", period=" + period + ", recentCount=" + recentCount + ", repairDate=" + repairDate
				+ ", possibleLocation=" + possibleLocation + ", intro=" + intro + ", content=" + content
				+ ", createdAt=" + createdAt + "]";
	}

}
