package com.kosta.geekku.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kosta.geekku.dto.CommunityBookmarkDto;
import com.kosta.geekku.dto.EstateBookMarkDto;
import com.kosta.geekku.dto.InteriorBookMarkDto;
import com.kosta.geekku.entity.CommunityBookmark;
import com.kosta.geekku.entity.Estate;
import com.kosta.geekku.entity.EstateBookmark;
import com.kosta.geekku.entity.InteriorBookmark;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.CommunityBookmarkRepository;
import com.kosta.geekku.repository.EstateBookmarkRepository;
import com.kosta.geekku.repository.InteriorBookmarkRepository;
import com.kosta.geekku.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookmarkServiceImpl implements BookmarkService {

	private final UserRepository userRepository;
	private final EstateBookmarkRepository estateBookmarkRepository;
	private final InteriorBookmarkRepository interiorBookmarkRepository;
	private final CommunityBookmarkRepository communityBookmarkRepository;

	@Override
	public Slice<EstateBookMarkDto> mypageEstatebookmarkList(Integer page, UUID userId) throws Exception {
		User user = userRepository.findById(userId).orElseThrow(() -> new Exception("일반회원찾기 오류"));

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "bookmarkEstateNum"));
		Slice<EstateBookMarkDto> pageInfo = estateBookmarkRepository.findAllByUserId(userId, pageable).map(EstateBookmark::toDto);
		
		return pageInfo;
	}

	@Override
	public Slice<InteriorBookMarkDto> mypageInteriorbookmarkList(Integer page, UUID userId) throws Exception {
		User user = userRepository.findById(userId).orElseThrow(() -> new Exception("일반회원찾기 오류"));

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "bookmarkInteriorNum"));
		Slice<InteriorBookMarkDto> pageInfo = interiorBookmarkRepository.findAllByUserId(userId, pageable).map(InteriorBookmark::toDto);

		return pageInfo;
	}

	@Override
	public Slice<CommunityBookmarkDto> mypageCommunitybookmarkList(Integer page, UUID userId) throws Exception {
		User user = userRepository.findById(userId).orElseThrow(() -> new Exception("일반회원찾기 오류"));

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "bookmarkInteriorNum"));
		Slice<CommunityBookmarkDto> pageInfo = communityBookmarkRepository.findAllByUser_UserId(userId, pageable).map(CommunityBookmark::toDto);

		return pageInfo;
	}

}
