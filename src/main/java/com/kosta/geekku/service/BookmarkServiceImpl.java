package com.kosta.geekku.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kosta.geekku.dto.EstateBookMarkDto;
import com.kosta.geekku.dto.InteriorBookMarkDto;
import com.kosta.geekku.entity.EstateBookmark;
import com.kosta.geekku.entity.InteriorBookmark;
import com.kosta.geekku.entity.User;
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

	@Override
	public Slice<EstateBookMarkDto> mypageEstatebookmarkList(Integer page, String userId) {
		Optional<User> user = userRepository.findById(UUID.fromString(userId));
		System.out.println(user);

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "bookmarkEstateNum"));
		System.out.println("pageable" + pageable);
		Slice<EstateBookMarkDto> pageInfo = estateBookmarkRepository.findAllByUserId(UUID.fromString(userId), pageable)
				.map(EstateBookmark::toDto);

		return pageInfo;
	}

	@Override
	public Slice<InteriorBookMarkDto> mypageInteriorbookmarkList(Integer page, String userId) throws Exception {
		Optional<User> user = userRepository.findById(UUID.fromString(userId));
		System.out.println(user);

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "bookmarkInteriorNum"));
		System.out.println("pageable" + pageable);
		Slice<InteriorBookMarkDto> pageInfo = interiorBookmarkRepository
				.findAllByUserId(UUID.fromString(userId), pageable).map(InteriorBookmark::toDto);

		return pageInfo;
	}

}
