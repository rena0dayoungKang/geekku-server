package com.kosta.geekku.entity;

import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
import org.hibernate.annotations.CreationTimestamp;

import com.kosta.geekku.dto.EstateDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Estate {
	// 부동산 매물등록
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer estateNum;

	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "companyId")
	private Company company;
	// private UUID companyId; // join column Company -companyId

	private String type; // 매물유형(farmHouse, countryHouse, apt, land)
	private String address1;
	private String address2;
	private String size1;
	private String size2;
	private Integer roomCount;
	private String rentType;
	private Integer jeonsePrice; // 전세가
	private Integer monthlyPrice; // 월세
	private Integer buyPrice; // 매매가
	private Integer depositPrice; // 보증금
	private Integer managePrice; // 관리비
	private Date availableDate;
	private boolean availableState; // 입주가능일 협의가능 유무-> 0: 불가능 1: 가능
	private Integer totalFloor;
	private Integer floor;
	private Integer bathCount;
	private Integer parking;
	private String utility;
	private String title;
	@Column(length = 1000)
	private String content;
	@CreationTimestamp
	private Timestamp createdAt;

	@OneToMany(mappedBy = "estate", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<EstateImage> imageList = new ArrayList<>();

	public EstateDto toDto() {
		EstateDto estateDto = EstateDto.builder().estateNum(estateNum).companyId(company.getCompanyId())
				.companyName(company.getCompanyName()).companyPhone(company.getPhone()).type(type).address1(address1)
				.address2(address2).size1(size1).size2(size2).roomCount(roomCount).rentType(rentType)
				.availableDate(availableDate).availableState(availableState).totalFloor(totalFloor).floor(floor)
				.bathCount(bathCount).utility(utility).title(title).content(content).createdAt(createdAt).build();

		if (imageList != null && imageList.size() > 0) {
			estateDto.setEstateImageNums(
					imageList.stream().map(i -> i.getEstateImageNum() + "").collect(Collectors.joining(",")));
		}

		if (company.getProfileImage() != null) {
			try {
				estateDto.setCompanyProfileImage(new String(Base64.encodeBase64(company.getProfileImage()), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}

		if (rentType.equals("jeonse")) {
			estateDto.setJeonsePrice(jeonsePrice);
		} else if (rentType.equals("monthly")) {
			estateDto.setDepositPrice(depositPrice);
			estateDto.setMonthlyPrice(monthlyPrice);
		} else {
			estateDto.setBuyPrice(buyPrice);
		}

		if (managePrice != null) {
			estateDto.setManagePrice(managePrice);
		}

		if (parking != null) {
			estateDto.setParking(parking);
		}

		return estateDto;
	}
}
