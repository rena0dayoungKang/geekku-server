package com.kosta.geekku.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

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
public class Interior {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer companyNum;

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

}
