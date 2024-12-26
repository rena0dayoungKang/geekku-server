package com.kosta.geekku.entity;

import java.sql.Timestamp;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.SampleDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class InteriorSample {
	// 인테리어 업체 시공사례
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sampleNum;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interiorNum")
	private Interior interior;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "companyId")
	private Company company;

	private String title;
	private String type; // 주거형태
	private String style;
	private Integer size;
	private String location;
	private String coverImage;
	@Column(columnDefinition = "LONGTEXT")
	@Lob
	private String content;
	@CreationTimestamp
	private Timestamp createdAt;

	public SampleDto toDto() {
		SampleDto sampleDto = SampleDto.builder().sampleNum(sampleNum).interiorNum(interior.getInteriorNum())
				.title(title).type(type).style(style).size(size).location(location).coverImage(coverImage)
				.intro(interior.getIntro()).companyName(interior.getCompany().getCompanyName())
				.companyId(interior.getCompany().getCompanyId()).content(content).createdAt(createdAt)
				.profileImage(company.getProfileImage())
				.build();

		return sampleDto;
	}
}
