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
public class InteriorRequest {
	// 인테리어 업체에 신청하기
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer requestNum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	// private UUID userId; //join column User - userId

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId")
	private Company company;
	// private UUID companyId; //join column Company -companyId

	private Integer period; // 희망시공일정 1.(2주~1달) 2. 3. 4. 5. (상담이후결정)
	private String type;
	private Integer statue; // 인테리어 공간상황
	private Integer size;
	private String name;
	private String phone;
	private Integer allowTime;
	private String content;
	@CreationTimestamp
	private Timestamp createdAt;

}
