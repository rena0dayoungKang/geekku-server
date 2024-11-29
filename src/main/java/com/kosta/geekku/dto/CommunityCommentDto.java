package com.kosta.geekku.dto;

import java.sql.Timestamp;
import java.util.UUID;

import com.kosta.geekku.entity.Community;
import com.kosta.geekku.entity.CommunityComment;
import com.kosta.geekku.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommunityCommentDto {
	private Integer commentNum;
	private UUID userId;
	private String userName;
	private String userNickname;
	private String userProfileImgStr;
    private String content;
    private Timestamp createdAt;
    private Integer communityNum;
    
    public CommunityComment toEntity() {
    	CommunityComment cc = CommunityComment.builder()
    			.commentNum(commentNum)
    			.community(Community.builder().communityNum(communityNum).build())
    			.user(User.builder().userId(userId).build())
    			.content(content)
    			.build();
    	return cc;
    }
}
