package com.kosta.geekku.entity;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
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

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import com.kosta.geekku.dto.HouseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class House {
	// 집꾸
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer houseNum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;
	// private UUID userId; //join column User - userId

	private String type;// 매물유형
	private String address1;
	private String address2;
	private String rentType; // 거래 종류(전세,월세,매매)
	private Integer size;
	private Integer jeonsePrice;
	private Integer monthlyPrice;
	private Integer buyPrice;
	private Date requestDate; // 입주희망 일자
	private boolean allowPhone;// 연락처 공개 여부-> 0:비공개 1:공개
	private String title;
	@Column(length = 1000)
	private String content; // 1000자
	@ColumnDefault("0")
	private Integer viewCount;
	@CreationTimestamp
	private Timestamp createdAt;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "house", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<HouseAnswer> houseAnswerList = new ArrayList<>();
	
	public HouseDto toDto() {
		HouseDto houseDto = HouseDto.builder()
				.houseNum(houseNum)
				.type(type)
				.address1(address1)
				.address2(address2)
				.size(size)
				.requestDate(requestDate)
				.allowPhone(allowPhone)
				.title(title)
				.content(content)
				.viewCount(viewCount)
				.createdAt(createdAt)
				.name(user.getName())
				.build();
		
		if (rentType.equals("jeonse")) {
			houseDto.setJeonsePrice(jeonsePrice);
		} else if (rentType.equals("monthly")) {
			houseDto.setMonthlyPrice(monthlyPrice);
		} else {
			houseDto.setBuyPrice(buyPrice);
		}
		
		if (allowPhone) {
			houseDto.setUserPhone(user.getPhone());
		}
		
		if (user.getProfileImage() != null) {
			try {
				houseDto.setUserProfileImage(new String(Base64.encodeBase64(user.getProfileImage()), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		
		return houseDto;
	}
}
