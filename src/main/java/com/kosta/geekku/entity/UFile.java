package com.kosta.geekku.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert
@Entity
public class UFile {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userImageNum;
	private String contentType;
	private String directory;
	private String name; //파일이름
	private Long size;
	@Column
	@CreationTimestamp
	private Date uploadDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "username")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Company company;
}
