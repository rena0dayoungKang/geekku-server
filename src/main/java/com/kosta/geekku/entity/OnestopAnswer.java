package com.kosta.geekku.entity;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class OnestopAnswer {
	// 한번에 꾸하기 답변
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer answerOnestopNum;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "oneStopNum")
	private Onestop onestop;
	//private Integer onestopNum; // join column Onestop -onestopNum
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId")
	private Company company;
	//private UUID companyId; //join column Company - companyId
	
	@Column(columnDefinition = "LONGTEXT")
	@Lob
	private String content;
	@CreationTimestamp
	private Timestamp createdAt;
}
