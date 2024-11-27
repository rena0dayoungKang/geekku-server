package com.kosta.geekku.service;

import org.springframework.data.domain.Slice;

import com.kosta.geekku.dto.EstateBookMarkDto;
import com.kosta.geekku.dto.InteriorBookMarkDto;

public interface BookmarkService {

	Slice<EstateBookMarkDto> mypageEstatebookmarkList(Integer page, String userId) throws Exception;

	Slice<InteriorBookMarkDto> mypageInteriorbookmarkList(Integer page, String userId) throws Exception;

	//Slice<CommunityBookMarkDto> mypageCommunitybookmarkList(Integer page, String userId) throws Exception;
}
