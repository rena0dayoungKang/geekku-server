package com.kosta.geekku.dto;

import java.sql.Timestamp;
import java.util.UUID;

import com.kosta.geekku.entity.Interior;
import com.kosta.geekku.entity.InteriorSample;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SampleDto {
	private Integer sampleNum;
	private Integer interiorNum;
	private String title;
	private String type;
	private String style;
	private Integer size;
	private String location;
	private Integer coverImage;
	private String intro;
	private String content;
	private Timestamp createdAt;
	private UUID companyId;
	private String companyName;

	public InteriorSample toEntity() {
		InteriorSample sample = InteriorSample.builder().sampleNum(sampleNum)
				.interior(Interior.builder()
						.interiorNum(interiorNum).intro(intro).build())
				.title(title)
				.type(type)
				.style(style)
				.size(size)
				.location(location)
				.coverImage(coverImage)
				.content(content)
				.createdAt(createdAt)
				.build();
		return sample;
	}
}
