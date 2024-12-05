package com.kosta.geekku.service;

import java.util.UUID;

import org.springframework.data.domain.Slice;

import com.kosta.geekku.dto.CommunityBookmarkDto;
import com.kosta.geekku.dto.EstateBookMarkDto;
import com.kosta.geekku.dto.InteriorBookMarkDto;

public interface BookmarkService {
	Slice<EstateBookMarkDto> mypageEstatebookmarkList(Integer page, UUID userId) throws Exception;
	Slice<InteriorBookMarkDto> mypageInteriorbookmarkList(Integer page, UUID userId) throws Exception;
	Slice<CommunityBookmarkDto> mypageCommunitybookmarkList(Integer page, UUID userId) throws Exception;
}
