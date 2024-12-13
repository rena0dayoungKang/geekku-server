package com.kosta.geekku.entity;

import java.io.UnsupportedEncodingException;
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

import org.apache.tomcat.util.codec.binary.Base64;
import com.kosta.geekku.dto.OnestopDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@DynamicInsert // 조회수
@Entity
public class Onestop {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer onestopNum;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "userId")
	private User user;
	// private UUID userId; //join column User - userId

	private String type;
	private String address1;
	private String address2;
	private String rentType;
	private Integer size;
	private Integer money;
	private boolean workType;
	private String interiorType;
	private Integer movePersons;
	private boolean allowPhone;
	private String title;
	@Column(length = 1000)
	private String content;
	@ColumnDefault("0")
	private Integer viewCount;
	@CreationTimestamp
	private Timestamp createdAt;

	@OneToMany(mappedBy = "onestop", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	private List<OnestopAnswer> onestopAnswerList = new ArrayList<>();

	public OnestopDto toDto() {
		OnestopDto onestopDto = OnestopDto.builder().onestopNum(onestopNum).type(type).address1(address1)
				.address2(address2).rentType(rentType).size(size).money(money).workType(workType)
				.interiorType(interiorType).movePersons(movePersons).allowPhone(allowPhone).title(title)
				.nickname(user.getNickname()).name(user.getName()).userId(user.getUserId()).userPhone(allowPhone ? user.getPhone() : null)
				.content(content).viewCount(viewCount).createdAt(createdAt).build();

		if (user.getProfileImage() != null) {
			try {
				onestopDto.setUserProfileImage(new String(Base64.encodeBase64(user.getProfileImage()), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		return onestopDto;
	}
}
