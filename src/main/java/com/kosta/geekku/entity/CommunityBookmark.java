package com.kosta.geekku.entity;

import java.util.Base64;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.kosta.geekku.dto.CommunityBookmarkDto;
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
public class CommunityBookmark {
	// 북마크 - 집들이
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer bookmarkCommunityNum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	// private UUID userId; // join column User - userID

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "communityNum")
	private Community community;
	// private Integer communityNum;

	public CommunityBookmark(Integer bookmarkCommunityNum, User userId) {
		this.bookmarkCommunityNum = bookmarkCommunityNum;
		this.user = userId;
	}
	
	public CommunityBookmarkDto toDto() {
		CommunityBookmarkDto communityBookmarkDto = CommunityBookmarkDto.builder()
				.bookmarkCommunityNum(bookmarkCommunityNum)
				.communityNum(community.getCommunityNum())
				.coverImage(community.getCoverImage())
				.title(community.getTitle())
				.name(community.getUser().getName())
				.nickname(community.getUser().getNickname())
				.profileImage(community.getUser().getProfileImage())
//				.user(user)
//				.community(community)
				.build();
		
		return communityBookmarkDto;
	}
}
