package com.kosta.geekku.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import com.kosta.geekku.dto.InteriorAllDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert // 조회수
@Entity
public class InteriorAllRequest {
	// 방꾸 신청
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer requestAllNum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	// private UUID userId; // join column User -userId

	private String name;
	private String phone;
	private String type;
	private Integer size;
	private String address1;
	private String address2;
	private Integer money;
	private boolean workType; // 시공종류 -> 0:부분시공 1:전체시공
	private String interiorType;
	private boolean allowPhone; // 연락처 공개 0:비공개 1:공개
	private String title;
	@ColumnDefault("0")
	private Integer viewCount;
	@Column(length = 1000)
	private String addContent;
	@CreationTimestamp
	private Timestamp createAt;

	@OneToMany(mappedBy = "interiorAll", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<InteriorAllRequest> interiorAll = new ArrayList<>();

	public InteriorAllDto toDto() {
		InteriorAllDto interiorAllDto = InteriorAllDto.builder().requestAllNum(requestAllNum)
				.name(user.getName()).nickname(user.getNickname()).phone(user.getPhone()).profileImage(user.getProfileImage())
				.type(type).size(size).address1(address1).address2(address2).money(money)
				.workType(workType).interiorType(interiorType).allowPhone(allowPhone).title(title)
				.addContent(addContent).viewCount(viewCount).createAt(createAt).build();
		return interiorAllDto;
	}

}
