package com.kosta.geekku.entity;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.tomcat.util.codec.binary.Base64;
import org.hibernate.annotations.CreationTimestamp;

import com.kosta.geekku.dto.InteriorAnswerDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class InteriorAllAnswer {
	// 방꾸 답변
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer answerAllNum;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requestAllNum")
	private InteriorAllRequest interiorAllRequest;
	// private Integer requestAllNum; //join column InteriorAllRequest -
	// requestAllNum

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "companyId")
	private Company company;
	// private UUID companyId; //join column Company - companyId

	private String content;
	@Column(length = 40)
	private String title;
	@CreationTimestamp
	private Timestamp createdAt;

	public InteriorAnswerDto toDto() {
		InteriorAnswerDto interiorAnswerDto = InteriorAnswerDto.builder().answerAllNum(answerAllNum).title(title).content(content)
				.createdAt(createdAt).requestAllNum(interiorAllRequest.getRequestAllNum())
				.companyId(company.getCompanyId()).companyName(company.getCompanyName())
				.companyPhone(company.getPhone()).build();

		if (company.getProfileImage() != null) {
			try {
				interiorAnswerDto
						.setCompanyProfileImage(new String(Base64.encodeBase64(company.getProfileImage()), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return interiorAnswerDto;
	}

}
