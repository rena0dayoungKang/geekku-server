package com.kosta.geekku.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.entity.Interior;
import com.kosta.geekku.entity.InteriorBookmark;
import com.kosta.geekku.entity.InteriorSample;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.InteriorBookmarkRepository;
import com.kosta.geekku.repository.InteriorDslRepository;
import com.kosta.geekku.repository.InteriorRepository;
import com.kosta.geekku.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InteriorSeviceImpl implements InteriorService {

	private final InteriorRepository interiorRepository;
	private final InteriorDslRepository interiorDslRepository;
	private final InteriorBookmarkRepository interiorBookmarkRepository;
	private final UserRepository userRepository;
	@Value("${upload.path}")
	private String uploadPath;

	@Override
	public List<InteriorDto> interiorListForMain() throws Exception {
		List<InteriorDto> interiorDtoList = null;
		interiorDtoList = interiorDslRepository.findInteriorListForMain().stream().map(i -> i.toDto())
				.collect(Collectors.toList());
		return interiorDtoList;
	}

	@Override
	public List<InteriorSample> sampleListForMain() throws Exception {
		List<InteriorSample> sampleList = null;
		sampleList = interiorDslRepository.findSampleListForMain();
		return sampleList;
	}

	@Override
	public InteriorDto interiorCompanyDetail(Integer num) throws Exception {
		Interior interior = interiorRepository.findById(num).orElseThrow(() -> new Exception("글번호 오류"));
		System.out.println("service" + num);
		// onestopDslRepository.updateOnestopViewCount(num, onestop.getViewCount() + 1);
		return interior.toDto();
	}
	public List<InteriorDto> interiorList(String possibleLocation) throws Exception {
		List<InteriorDto> interiorDtoList = null;
		Long allCnt = 0L;
		if(possibleLocation==null) {
			interiorDtoList = interiorDslRepository.interiorListAll()
					.stream().map(i->i.toDto()).collect(Collectors.toList());
			allCnt = interiorDslRepository.interiorCountAll();
		} else {
			interiorDtoList = interiorDslRepository.interiorListByLoc(possibleLocation)
					.stream().map(i->i.toDto()).collect(Collectors.toList());
			allCnt = interiorDslRepository.interiorCountByLoc(possibleLocation);
		}
		return interiorDtoList;
	}

	@Override
	public Integer checkBookmark(String userId, Integer interiorNum) throws Exception {
		return interiorDslRepository.findInteriorBookmark(UUID.fromString(userId), interiorNum);
	}

	@Override
	@Transactional
	public boolean toggleBookmark(String userId, Integer interiorNum) throws Exception {
		InteriorBookmark interiorBookmark = interiorBookmarkRepository.findByInteriorNumAndUserId(interiorNum, UUID.fromString(userId));
		System.out.println(interiorBookmark);

		if(interiorBookmark==null) {
			interiorBookmarkRepository.save(InteriorBookmark.builder().userId(UUID.fromString(userId)).interiorNum(interiorNum).build());
			return true;
		} else {
			interiorBookmarkRepository.deleteById(interiorBookmark.getBookmarkInteriorNum());
			return false;	
		}
	}

	@Override
	public Integer interiorRegister(InteriorDto interiorDto) throws Exception {
		Interior interior = interiorDto.toEntity();
		interiorRepository.save(interior);
		return interior.getInteriorNum();
	}
}
