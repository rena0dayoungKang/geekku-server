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
public class InteriorBookmark {
	// 북마크 - 인테리어업자 follow
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bookmarkInteriorNum;
	
	@Column(columnDefinition = "BINARY(16)", unique = true, nullable = false)
	private UUID userId;
	//private UUID userId; // join column User - userID
	
	private Integer interiorNum;
	//private UUID companyId; // join column Company - companyId
}
