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
public class EstateImage {
	// 부동산 매물 등록 게시판의 이미지 테이블
	// upload/image/estate 경로
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer estateImageNum;
	private String contentType;
	private String directory;
	private String name; //파일이름
	private Long size;
	private Date uploadDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estateNum")
	private Estate estate;
	// private Integer estateNum; //join column Estate - estateNum
}
