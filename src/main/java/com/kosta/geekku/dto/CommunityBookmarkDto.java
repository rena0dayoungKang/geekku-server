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
	private Community community;

	public void CommunityBookmark(Integer bookmarkCommunityNum, User userId) {
		this.bookmarkCommunityNum = bookmarkCommunityNum;
		this.user = userId;
	}
	
	private String coverImage;
	private String name;
	private String nickname;
	private byte[] profileImage;
	private String title;
	private Integer communityNum;
}