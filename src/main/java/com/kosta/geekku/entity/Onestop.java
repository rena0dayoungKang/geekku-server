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

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.kosta.geekku.dto.OnestopDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Onestop {
	// �븳踰덉뿉 袁명븯湲�
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer onestopNum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	// private UUID userId; //join column User - userId

	private String type;
	private String address1;
	private String address2;
	private String rentType;
	private Integer size;
	private Integer money;
	private String workType;
	private String interiorType;
	private Integer movePersons;
	private Integer allowPhone;
	private String title;
	@Column(length = 1000)
	private String content;
	@ColumnDefault("0")
	private Integer viewCount;
	@CreationTimestamp
	private Timestamp createdAt;

	public OnestopDto toDto() {
		OnestopDto onestopDto = OnestopDto.builder().onestopNum(onestopNum).user(user).type(type).address1(address1)
				.address2(address2).rentType(rentType).size(size).money(money).workType(workType)
				.interiorType(interiorType).movePersons(movePersons).allowPhone(allowPhone).title(title)
				.content(content).viewCount(viewCount).createdAt(createdAt).build();
		return onestopDto;
	}
}
