package com.kosta.geekku.dto;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
public class MessageDto {
    private Integer num;            // 알람 번호
    private String message;         // 알람 메시지
    private UUID receiver;          // 수신자 ID
    private String username;        // 수신자 이름
    private String companyName;     // 회사 이름
    private String type;            // 알람 타입
    private Integer detailPath;     // 상세 경로
    private Date createAt;          // 생성 시간
    private UUID sender;            // 발신자 ID
    private String title;           // 제목
}
