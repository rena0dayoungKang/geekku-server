package com.kosta.geekku.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kosta.geekku.dto.OnestopAnswerDto;
import com.kosta.geekku.dto.OnestopDto;
import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.Onestop;
import com.kosta.geekku.entity.OnestopAnswer;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.repository.OnestopAnswerRepository;
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
	private final OnestopAnswerRepository onestopAnswerRepository;
	private final CompanyRepository companyRepository;

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
		pageInfo.setTotalCount(allCnt);
		return onestopDtoList;
	}

	@Transactional
	@Override
	public Integer onestopWrite(OnestopDto onestopDto) throws Exception {
		Onestop onestop = onestopDto.toEntity();
		System.out.println(onestop);
		onestopRepository.save(onestop);

		return onestop.getOnestopNum();
	}

	@Transactional
	@Override
	public OnestopDto onestopDetail(Integer onestopNum) throws Exception {
		Onestop onestop = onestopRepository.findById(onestopNum).orElseThrow(() -> new Exception("글번호 오류"));
		onestopDslRepository.updateOnestopViewCount(onestopNum, onestop.getViewCount() + 1);
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
	public void onestopDelete(Integer onestopNum) throws Exception {
		onestopRepository.findById(onestopNum).orElseThrow(() -> new Exception("글 번호 오류"));
		onestopRepository.deleteById(onestopNum);

	}

	@Override
	public Integer onestopAnswerWrite(OnestopAnswerDto onestopAnswerDto, UUID companyId) throws Exception {
		Onestop onestop = onestopRepository.findById(onestopAnswerDto.getOnestopNum())
				.orElseThrow(() -> new Exception("한번에꾸하기 글 번호 오류"));
		Company company = companyRepository.findById(companyId).orElseThrow(() -> new Exception("기업회원 찾기 오류"));
		OnestopAnswer onestopAnswer = onestopAnswerDto.toEntity();
		onestopAnswer.setCompany(company);
		onestopAnswerRepository.save(onestopAnswer);
		
		return onestopAnswer.getAnswerOnestopNum();

	}

	@Transactional
	@Override
	public List<OnestopAnswerDto> onestopAnswerList(PageInfo pageInfo, Integer onestopNum) throws Exception {
		Onestop onestop = onestopRepository.findById(onestopNum).orElseThrow(() -> new Exception("집꾸 글번호 오류"));
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);

		List<OnestopAnswerDto> onestopAnswerDtoList = onestopDslRepository.onestopAnswerListByPaging(pageRequest)
				.stream().map(a -> a.toDto()).collect(Collectors.toList());
		Long cnt = onestopDslRepository.findOnestopCount();

		Integer allPage = (int) (Math.ceil(cnt.doubleValue() / pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);

		return onestopAnswerDtoList;
	}

	@Transactional
	@Override
	public void onestopAnswerDelete(Integer onestopAnswerNum, Integer onestopNum) throws Exception {
		onestopAnswerRepository.findById(onestopAnswerNum).orElseThrow(() -> new Exception("답변이 존재하지 않습니다."));
		onestopAnswerRepository.deleteById(onestopAnswerNum);

	}

	@Override
	public Slice<OnestopAnswerDto> onestopAnswerListForMypage(int page, String companyId) throws Exception {

		Optional<Company> company = companyRepository.findById(UUID.fromString(companyId));

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
		Slice<OnestopAnswerDto> pageInfo = onestopAnswerRepository.findAllByCompany(company, pageable)
				.map(OnestopAnswer::toDto);

		return pageInfo;
	}

	@Override
	public Page<OnestopDto> onestopListForUserMypage(int page, int size, UUID userId) throws Exception {
		Optional<User> user = userRepository.findById(userId);

		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<OnestopDto> pageInfo = onestopRepository.findAllByUser(user, pageable).map(Onestop::toDto);

		return pageInfo;
	}

}
