package com.kosta.geekku.entity;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kosta.geekku.dto.CommunityDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Community {
	// 집들이
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer communityNum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	// private UUID userId; //join column User - userId

	private String type;
	private Integer size;
	private String address1;
	private String address2;
	private String familyType;
	private String interiorType;
	private Date periodStartDate;
	private Date periodEndDate;
	private Integer money;
	private String style;
	@Column(name = "cover_image")
	private String coverImage;
	private String title;
	@Column(columnDefinition = "LONGTEXT")
	@Lob
	private String content;
	@CreationTimestamp
	private Timestamp createdAt;
	@ColumnDefault("0")
	private Integer viewCount;
	
	
	
	public CommunityDto toDto() {
		boolean isOwner = this.user != null && this.user.getUserId().equals(user.getUserId());
	    return CommunityDto.builder()
	        .communityNum(communityNum) // 커뮤니티 번호
	        .title(title)              // 게시글 제목
	        .content(content)          // 게시글 내용
	        .type(type)                // 주거 형태
	        .size(size)                // 평수
	        .address1(address1)        // 주소 1 (시/도)
	        .address2(address2)        // 주소 2 (구/동)
	        .familyType(familyType)    // 가족 형태
	        .interiorType(interiorType) // 인테리어 타입
	        .periodStartDate(periodStartDate) // 시공 시작일
	        .periodEndDate(periodEndDate)     // 시공 종료일
	        .money(money)              // 예산
	        .style(style)              // 스타일
	        .coverImage(coverImage)    // 커버 이미지 ID
	        .createdAt(createdAt)      // 생성 시간
	        .viewCount(viewCount)      // 조회수
	        .username(user != null ? user.getUsername() : null) // 작성자 이름 
	        .isOwner(isOwner)
	        .build();
	}

}
