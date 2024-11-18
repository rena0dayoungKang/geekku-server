package com.kosta.geekku.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

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
}
