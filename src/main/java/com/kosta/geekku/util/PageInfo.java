package com.kosta.geekku.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageInfo {

    private Integer curPage = 1;
    private Integer allPage = 0;
    private Integer startPage = 1;
    private Integer endPage = 1;
    private Integer pageSize = 10;

    public void calculatePages() {
        this.startPage = ((this.curPage - 1) / 10) * 10 + 1;
        this.endPage = Math.min(this.startPage + 9, this.allPage);
    }
}
