package com.kosta.geekku.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.hibernate.annotations.CreationTimestamp;

import com.kosta.geekku.dto.SampleDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class InteriorSample {
	// 인테리어 업체 시공사례
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer sampleNum;

//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "interiorNum")
//	private Interior interior;

	private Integer interiorNum;

	private String type; // 주거형태
	private String style;
	private Integer size;
	private String location;
	private Integer coverImage;// 한장이라서 Integer
	private String intro;
	@Column(columnDefinition = "LONGTEXT")
	@Lob
	private String content;
	@CreationTimestamp
	private Timestamp createdAt;

	public SampleDto toDto() {
		SampleDto sampleDto = SampleDto.builder().sampleNum(sampleNum).interiorNum(interiorNum).type(type)
				.style(style).size(size).location(location).coverImage(coverImage).intro(intro)
				.content(content).createdAt(createdAt).build();
		return sampleDto;
	}
}
