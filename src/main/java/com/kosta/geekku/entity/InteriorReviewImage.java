package com.kosta.geekku.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class InteriorReviewImage {
	// 인테리어 업체 리뷰에 사용되는 이미지 테이블
	// upload/image/interior 경로
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer interiorReviewImageNum;
	private String contentType;
	private String dierctory;
	private String name; // 파일이름
	private Long size;
	private Date uploadDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reviewNum")
	private InteriorReview interiorReview;
	// private Integer reviewNum; //join column InteriorReview - reviewNum
}
