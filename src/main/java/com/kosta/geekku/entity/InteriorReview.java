package com.kosta.geekku.entity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;

import com.kosta.geekku.dto.ReviewDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class InteriorReview {
	// 인테리어 업체 리뷰
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer reviewNum;

	@ManyToOne(fetch = FetchType.LAZY) // 나중에 cascade 확인
	@JoinColumn(name = "userId")
	private User user;
	// private UUID userId; //join column User - userId

	private String companyName;

	private LocalDate  date;
	private String type;
	private String style;
	private Integer size;
	private String location;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST) // 나중에 cascade 확인
	@JoinColumn(name = "interiorNum")
	private Interior interior;
	// private UUID companyId; //join column Company -companyId

	@OneToMany(mappedBy = "interiorReview", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<InteriorReviewImage> imagesList = new ArrayList<>();

	@Column(length = 500)
	private String content;
	@CreationTimestamp
	private Timestamp createdAt;

	public ReviewDto toDto() {
		ReviewDto reviewDto = ReviewDto.builder().reviewNum(reviewNum).userId(user.getUserId()).name(user.getName())
				.nickName(user.getNickname()).profileImage(user.getProfileImage()).companyName(interior.getCompany().getCompanyName()).type(type).date(date)
				.style(style).size(size).location(location).content(content).createdAt(createdAt)
				.interiorNum(interior.getInteriorNum()).build();

		if (imagesList != null && imagesList.size() > 0) {
			reviewDto.setImageNums(
					imagesList.stream().map(i -> i.getInteriorReviewImageNum() + "").collect(Collectors.joining(",")));
		}

		return reviewDto;
	}

}
