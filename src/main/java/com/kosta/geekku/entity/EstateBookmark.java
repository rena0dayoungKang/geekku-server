package com.kosta.geekku.entity;

import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.kosta.geekku.dto.EstateBookMarkDto;

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
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bookmarkEstateNum;
	@Column(unique = false)
	private UUID userId;
	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estate_num")
    private Estate estate;

	public EstateBookMarkDto toDto() {
		EstateBookMarkDto estateBookmarkDto = EstateBookMarkDto.builder()
				.bookmarkEstateNum(bookmarkEstateNum)
				.userId(userId)
				.estateNum(estate.getEstateNum())
				.type(estate.getType())
				.jibunAddress(estate.getJibunAddress())
				.size1(estate.getSize1())
				.size2(estate.getSize2())
				.jeonsePrice(estate.getJeonsePrice())
				.monthlyPrice(estate.getMonthlyPrice())
				.buyPrice(estate.getBuyPrice())
				.depositPrice(estate.getDepositPrice())
				.title(estate.getTitle())
				.build();
		
		if (estate.getImageList() != null && estate.getImageList().size() > 0) {
			estateBookmarkDto.setEstateImageNums(
					estate.getImageList().stream().map(i -> i.getEstateImageNum() + "").collect(Collectors.joining(",")));
		}

		return estateBookmarkDto;
	}
}
