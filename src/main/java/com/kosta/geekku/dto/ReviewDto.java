package com.kosta.geekku.dto;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.UUID;

import com.kosta.geekku.entity.Interior;
import com.kosta.geekku.entity.InteriorReview;
import com.kosta.geekku.entity.User;

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
	private LocalDate date;
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
				.companyName(getCompanyName())
				.date(date)
				.type(type)
				.style(style)
				.size(size)
				.location(location)
				.content(content)
				.createdAt(createdAt)
				.user(User.builder().userId(getUserId()).build())
				.interior(Interior.builder().interiorNum(getInteriorNum()).build())
				.build();
		return review;
	}
}
