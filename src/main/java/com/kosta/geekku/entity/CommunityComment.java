package com.kosta.geekku.entity;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.annotations.CreationTimestamp;

import com.kosta.geekku.dto.CommunityCommentDto;
import com.kosta.geekku.dto.UserDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CommunityComment {
	// 집들이 댓글
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer commentNum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "communityNum")
	private Community community;
	// private Integer communityNum; // join column Community -communityNum

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	// private UUID userId; //join column User - userId

	@Column(length = 500)
	private String content; // 500자 제한
	@CreationTimestamp
	private Timestamp createdAt;
	
	public CommunityCommentDto toDto() {
		CommunityCommentDto ccd = CommunityCommentDto.builder()
				.commentNum(commentNum)
				.userId(user.getUserId())
				.userName(user.getUsername())
				.userNickname(user.getNickname())
				.content(content)
				.createdAt(createdAt)
				.communityNum(community.getCommunityNum())
				.build();
		if(user.getProfileImage()!=null) {
			try {
				ccd.setUserProfileImgStr(new String(Base64.encodeBase64(user.getProfileImage()), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return ccd;
	}
}
