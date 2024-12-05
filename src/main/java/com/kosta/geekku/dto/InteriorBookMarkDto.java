package com.kosta.geekku.dto;

import java.util.UUID;

import com.kosta.geekku.entity.InteriorBookmark;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InteriorBookMarkDto {
	private Integer bookmarkInteriorNum;
	private UUID userId;
	private Integer interiorNum;
	
	private String interiorImageStr;
	private String companyName;
	private String possibleLocation;
	private boolean possiblePart;
}
