package com.kosta.geekku.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommunityFilterDto {

    private String type; // 주거형태

    private Integer size; // 평수

    private String style; // 스타일

    private LocalDate periodStart; // 기간 시작

    private LocalDate periodEnd; // 기간 끝

    private Integer money; // 예산
}
