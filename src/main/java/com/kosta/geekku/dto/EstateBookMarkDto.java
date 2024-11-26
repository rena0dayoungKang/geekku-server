package com.kosta.geekku.dto;

import java.util.UUID;

import com.kosta.geekku.entity.EstateBookmark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstateBookMarkDto {
	private Integer bookmarkEstateNum;
	private UUID userId;
	private Integer estateNum;

	public EstateBookmark toEntity() {
		return EstateBookmark.builder().bookmarkEstateNum(bookmarkEstateNum).userId(userId).estateNum(estateNum)
				.build();
	}
}
