package com.kosta.geekku.dto;

import com.kosta.geekku.entity.Community;
import com.kosta.geekku.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityBookmarkDto {
	private Integer bookmarkCommunityNum;

	private User user;
	// private UUID userId; // join column User - userID

	private Community community;
	// private Integer communityNum;

	public void CommunityBookmark(Integer bookmarkCommunityNum, User userId) {
		this.bookmarkCommunityNum = bookmarkCommunityNum;
		this.user = userId;
	}
}
