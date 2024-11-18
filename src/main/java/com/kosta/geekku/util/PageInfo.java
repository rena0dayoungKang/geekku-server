package com.kosta.geekku.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
	private Integer curPage;
	private Integer allPage;
	private Integer startPage;
	private Integer endPage;
	
}
