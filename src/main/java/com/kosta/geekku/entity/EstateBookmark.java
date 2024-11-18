package com.kosta.geekku.entity;

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
public class EstateBookmark {
	// ºÏ¸¶Å© - ¸Å¹°
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bookmarkEstateNum;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	//private UUID userId; //join column User - userID
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "estateNum")
	private Estate estate;
	//private Integer estateNum; //join column Estate - estateNum
}
