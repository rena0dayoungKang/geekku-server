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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
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
	private Integer coverImage;
	private String title;
	@Column(columnDefinition = "LONGTEXT")
	@Lob
	private String content;
	@CreationTimestamp
	private Timestamp createdAt;
	@ColumnDefault("0")
	private Integer viewCount;

}
