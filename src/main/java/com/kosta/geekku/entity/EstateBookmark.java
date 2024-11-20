package com.kosta.geekku.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
	// 북마크 - 매물
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bookmarkEstateNum;
	@Column
	private UUID userId;
	@Column
	private Integer estateNum;
}
