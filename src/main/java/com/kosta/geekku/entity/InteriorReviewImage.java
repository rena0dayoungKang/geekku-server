package com.kosta.geekku.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
	@Column
	private String contentType;
	@Column
	private String dierctory;
	@Column
	private String name; // 파일이름
	@Column
	private Long size;
	@Column
	@CreationTimestamp
	private Date uploadDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reviewNum")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private InteriorReview interiorReview;

	
}
