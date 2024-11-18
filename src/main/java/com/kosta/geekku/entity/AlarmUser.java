package com.kosta.geekku.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
public class AlarmUser {
	// 부동산, 인테리어회사가 집꾸,방꾸,원스탑 답변시 생성
	// 일반사용자 알림 //부동산, 인테리어회사가 집꾸,방꾸,원스탑 답변시 생성
	// 일반사용자 알림
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userAlarmNum;
	private String message;
	@CreationTimestamp
	private Timestamp createdAt;
	private boolean status; // 0:������ 1:����

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	// private UUID userId; //join column User - userId

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId")
	private Company company;
	// private UUID companyId; //join column Company -companyId

	private String type; // house, interior, onestop
	private Integer requestNum;
	private Integer answerNum;
}
