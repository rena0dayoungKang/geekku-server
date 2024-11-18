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
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class InteriorAllAnswer {
	// 방꾸 답변
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer answerAllNum;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requestAllNum")
	private InteriorAllRequest interiorAllRequest;
	//private Integer requestAllNum; //join column InteriorAllRequest - requestAllNum
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId")
	private Company company;
	//private UUID companyId; //join column Company - companyId
	
	private String content;
	@CreationTimestamp
	private Timestamp createdAt;

}
