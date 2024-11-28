package com.kosta.geekku.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import com.kosta.geekku.dto.InteriorRequestDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class InteriorRequest {
	// 인테리어 업체에 신청하기
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer requestNum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "interiorNum")
	private Interior interior;

	private Integer period; // 희망시공일정 1.(2주~1달) 2. 3. 4. 5. (상담이후결정)
	private Integer type;
	private Integer status; // 인테리어 공간상황
	private Integer size;
	private String name;		//integer 형식으로 저장 후, 프론트에서 integer에 해당하는 값 문자열출력
	private String phone;
	private Integer allowTime;
	private String content;
	@CreationTimestamp
	private Timestamp createdAt;

	public InteriorRequestDto toDto() {
		InteriorRequestDto requestDto = InteriorRequestDto.builder()
				.requestNum(requestNum)
				.userId(user.getUserId())
				.name(user.getName())
				.companyId(interior.getCompany().getCompanyId())
				.interiorNum(interior.getInteriorNum())
				.period(period)
				.type(type)
				.status(status)
				.size(size)
				.name(name)
				.allowTime(allowTime)
				.content(content)
				.createdAt(createdAt)
				.build();
		return requestDto;
	}
	
}
