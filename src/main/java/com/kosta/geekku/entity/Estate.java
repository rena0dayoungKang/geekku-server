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
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

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
public class Estate {
	// 부동산 매물등록
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer estateNum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId")
	private Company company;
	// private UUID companyId; // join column Company -companyId

	private String type; // 매물유형(farmHouse, countryHouse, apt, land)
	private String address1;
	private String address2;
	private String size1;
	private String size2;
	private Integer roomCount;
	private String rentType;
	private Integer jeonsePrice;
	private Integer monthlyPrice;
	private Integer buyPrice;
	private Integer depositPrice;
	private Integer managePrice;
	private Date availableDate;
	private boolean availableState; // 입주가능일 협의가능 유무-> 0: 불가능 1: 가능
	private Integer totalFloor;
	private Integer floor;
	private Integer bathCount;
	private Integer parking;
	private String utility;
	private String title;
	@Column(columnDefinition = "LONGTEXT")
	@Lob
	private String content;
	@CreationTimestamp
	private Timestamp createdAt;

}
