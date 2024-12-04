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
	
    private String type;
    private String jibunAddress;
	private String size1;
	private String size2;
	private String estateImageNums;
	private Integer jeonsePrice;
	private Integer monthlyPrice;
	private Integer buyPrice;
	private Integer depositPrice;
	private String title;
}
