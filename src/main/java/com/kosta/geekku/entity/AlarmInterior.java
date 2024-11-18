package com.kosta.geekku.entity;

import java.sql.Timestamp;

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
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class AlarmInterior {
	// 사용자 인테리어 문의시 알림 생성
	// 인테리어 알림
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer interiorAlarmNum;
	private String message;
	private Timestamp createdAt;
	private boolean status; // 0:안읽음 1:읽음

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	// private UUID userId; //join column User - userId

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId")
	private Company company;
	// private UUID companyId; //join column Company - companyId

	private Integer requestNum;
}
