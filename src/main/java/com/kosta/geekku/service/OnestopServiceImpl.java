package com.kosta.geekku.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.geekku.dto.OnestopAnswerDto;
import com.kosta.geekku.dto.OnestopDto;
import com.kosta.geekku.dto.ReviewDto;
import com.kosta.geekku.entity.InteriorReview;
import com.kosta.geekku.entity.Onestop;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.OnestopDslRepository;
import com.kosta.geekku.repository.OnestopRepository;
import com.kosta.geekku.repository.UserRepository;
import com.kosta.geekku.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OnestopServiceImpl implements OnestopService {

	private final OnestopRepository onestopRepository;
	private final OnestopDslRepository onestopDslRepository;
	private final UserRepository userRepository;

	@Override
	public List<OnestopDto> onestopList(PageInfo pageInfo, String type, String word) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);
		List<OnestopDto> onestopDtoList = null;
		Long allCnt = 0L;
		if (word == null || word.trim().equals("")) { // 전체 목록
			onestopDtoList = onestopDslRepository.findOnestopListByPaging(pageRequest).stream().map(b -> b.toDto())
					.collect(Collectors.toList());
			allCnt = onestopDslRepository.findOnestopCount();
		} else { // 검색
			onestopDtoList = onestopDslRepository.searchOnestopListByPaging(pageRequest, type, word).stream()
					.map(b -> b.toDto()).collect(Collectors.toList());
			allCnt = onestopDslRepository.searchOnestopCount(type, word);
		}

		Integer allPage = (int) (Math.ceil(allCnt.doubleValue() / pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		return onestopDtoList;
	}

	@Transactional
	@Override
	public Integer onestopWrite(OnestopDto onestopDto) throws Exception {
		Onestop onestop = onestopDto.toEntity();
		onestopRepository.save(onestop);

		return onestop.getOnestopNum();
	}

	@Override
	public OnestopDto onestopDetail(Integer num) throws Exception {
		Onestop onestop = onestopRepository.findById(num).orElseThrow(() -> new Exception("글번호 오류"));
		System.out.println("service" + num);
		// onestopDslRepository.updateOnestopViewCount(num, onestop.getViewCount() + 1);
		return onestop.toDto();
	}

	/*
	 * @Override public Integer onestopModify(OnestopDto onestopDto) throws
	 * Exception { Onestop onestop =
	 * onestopRepository.findById(onestopDto.getOnestopNum()) .orElseThrow(() -> new
	 * Exception("글번호 오류")); System.out.println(onestop.getOnestopNum());
	 * onestop.setTitle(onestopDto.getTitle());
	 * onestop.setContent(onestopDto.getContent()); onestopRepository.save(onestop);
	 * 
	 * return onestop.getOnestopNum(); }
	 */

	@Override
	@Transactional
	public void onestopDelete(Integer num) throws Exception {
		// boardLikeRepository.deleteByBoardNum(num);
		onestopRepository.deleteById(num);

	}

	@Override
	public Integer onestopAnswerWrite(OnestopAnswerDto onestopAnswerDto, Integer onestopNum) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<OnestopAnswerDto> houseAnswerList(PageInfo pageInfo, Integer onestopNum) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onestopAnswerDelete(Integer onestopAnswerNum, Integer onestopNum) throws Exception {
		// TODO Auto-generated method stub

	}
	
	@Override
	public Page<OnestopDto> onestopListForUserMypage(int page, int size, String userId) throws Exception {
		Optional<User> user = userRepository.findById(UUID.fromString(userId));
		
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<OnestopDto> pageInfo = onestopRepository.findAllByUser(user, pageable).map(Onestop::toDto);
		
		return pageInfo;
	}

}
