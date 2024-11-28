package com.kosta.geekku.dto;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageDto {
	private Integer num;
	private String message;
	private UUID receiver; 
	private String type;
	private Integer detailPath;
	private Date createAt;
	private UUID sender;

}