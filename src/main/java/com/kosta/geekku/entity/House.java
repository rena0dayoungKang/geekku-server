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
import org.hibernate.annotations.DynamicInsert;

import com.kosta.geekku.dto.HouseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DynamicInsert
@Entity
public class House {
	// 吏묎씀
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer houseNum;

	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "userId")
	private User user;
	// private UUID userId; //join column User - userId

	private String type;// 留ㅻЪ�쑀�삎
	private String address1;
	private String address2;
	private String rentType; // 嫄곕옒 醫낅쪟(�쟾�꽭,�썡�꽭,留ㅻℓ)
	private Integer size;
	private Integer jeonsePrice;
	private Integer monthlyPrice;
	private Integer depositPrice;
	private Integer buyPrice;
	private Date requestDate; // �엯二쇳씗留� �씪�옄
	private boolean requestState; // �엯二쇳씗留� �씪�옄 誘몄젙 -> 0:�젙�븿 1:誘몄젙 
	private boolean allowPhone;// �뿰�씫泥� 怨듦컻 �뿬遺�-> 0:鍮꾧났媛� 1:怨듦컻
	private String title;
	@Column(length = 1000)
	private String content; // 1000�옄
	@ColumnDefault("0")
	private Integer viewCount;
	@CreationTimestamp
	private Timestamp createdAt;
	
	@OneToMany(mappedBy = "house", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private List<HouseAnswer> houseAnswerList = new ArrayList<>();
	
	public HouseDto toDto() {
		HouseDto houseDto = HouseDto.builder()
				.houseNum(houseNum)
				.type(type)
				.address1(address1)
				.address2(address2)
				.rentType(rentType)
				.size(size)
				.requestDate(requestDate)
				.requestState(requestState)
				.allowPhone(allowPhone)
				.userPhone(allowPhone ? user.getPhone() : null)
				.title(title)
				.content(content)
				.viewCount(viewCount)
				.createdAt(createdAt)
				.nickname(user.getNickname())
				.build();
		
		if (rentType.equals("jeonse")) {
			houseDto.setJeonsePrice(jeonsePrice);
		} else if (rentType.equals("monthly")) {
			houseDto.setMonthlyPrice(monthlyPrice);
			houseDto.setDepositPrice(depositPrice);
		} else {
			houseDto.setBuyPrice(buyPrice);
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
