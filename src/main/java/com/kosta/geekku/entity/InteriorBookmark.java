package com.kosta.geekku.entity;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.kosta.geekku.dto.InteriorBookMarkDto;

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

	/*
	 * @ManyToOne(fetch = FetchType.LAZY)
	 * 
	 * @JoinColumn(name = "userId") private User user;
	 */

	@Column
	private UUID userId;
	// private UUID userId; // join column User - userID

	private Integer interiorNum;
	// private UUID companyId; // join column Company - companyId

	public InteriorBookMarkDto toDto() {
		InteriorBookMarkDto interiorBookmarkDto = InteriorBookMarkDto.builder().bookmarkInteriorNum(bookmarkInteriorNum)
				.userId(userId).interiorNum(interiorNum).build();

		return interiorBookmarkDto;
	}

}
