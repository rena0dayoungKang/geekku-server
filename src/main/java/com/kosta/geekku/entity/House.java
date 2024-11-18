package com.kosta.geekku.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class House {
	// 집꾸
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer houseNum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	// private UUID userId; //join column User - userId

	private String type;// 매물유형
	private String address1;
	private String address2;
	private String rentType; // 거래 종류(전세,월세,매매)
	private Integer size;
	private Integer jeonsePrice;
	private Integer monthlyPrice;
	private Integer buyPrice;
	private Date requestDate; // 입주희망 일자
	private boolean allowPhone;// 연락처 공개 여부-> 0:비공개 1:공개
	private String title;
	@Column(length = 1000)
	private String content; // 1000자
	@ColumnDefault("0")
	private Integer viewCount;
	@CreationTimestamp
	private Timestamp createdAt;

}
