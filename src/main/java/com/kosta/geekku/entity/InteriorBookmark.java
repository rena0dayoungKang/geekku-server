package com.kosta.geekku.entity;

import java.util.Base64;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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

	@Column(unique = false)
	private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interior_num")
    private Interior interior;

	public InteriorBookMarkDto toDto() {
		InteriorBookMarkDto interiorBookmarkDto = InteriorBookMarkDto.builder()
				.bookmarkInteriorNum(bookmarkInteriorNum)
				.userId(userId)
				.interiorNum(interior.getInteriorNum())
				.companyName(interior.getCompany().getCompanyName())
				.possibleLocation(interior.getPossibleLocation())
				.possiblePart(interior.isPossiblePart())
				.build();
		
		if (interior.getCoverImage() != null) {
			try {
				String encodedImage = Base64.getEncoder().encodeToString(interior.getCoverImage()); // Base64 인코딩
				interiorBookmarkDto.setInteriorImageStr(encodedImage);
			} catch(Exception e) {
				e.printStackTrace();
				throw new RuntimeException("이미지 인코딩 중 오류 발생", e);
			}
		}
		
		return interiorBookmarkDto;
	}

}
