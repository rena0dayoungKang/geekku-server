package com.kosta.geekku.dto;

import java.sql.Timestamp;
import java.util.UUID;

import com.kosta.geekku.entity.InteriorReview;
import com.kosta.geekku.entity.InteriorReviewImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
	private Integer reviewNum;
	private UUID userId;
	private String companyName;
	private String type;
	private String style;
	private Integer size;
	private String location;
	private String imageNums;
	private String content;
	private Timestamp createdAt;
	private Integer interiorNum;

	
	public InteriorReview toEntity() {
		InteriorReview review = InteriorReview.builder()
				.reviewNum(reviewNum)
				.companyName(companyName)
				.type(type)
				.style(style)
				.size(size)
				.location(location)
				.content(content)
				.createdAt(createdAt)
				.build();
		return review;
	}
}
