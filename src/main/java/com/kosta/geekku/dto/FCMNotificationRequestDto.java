package com.kosta.geekku.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FCMNotificationRequestDto {
	private Integer num;
	private String targetUserId;
	private String title;
	private String body;
	private String fcmToken;
}
