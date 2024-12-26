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
import org.hibernate.annotations.DynamicInsert;
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
@DynamicInsert
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
	@Column
	@CreationTimestamp
	private Date uploadDate;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "estateNum")
	@OnDelete(action=OnDeleteAction.CASCADE)
	private Estate estate;
	// private Integer estateNum; //join column Estate - estateNum

	@Override
	public String toString() {
		return "EstateImage [estateImageNum=" + estateImageNum + ", contentType=" + contentType + ", directory="
				+ directory + ", name=" + name + ", uploadDate=" + uploadDate + "]";
	}
	
}
