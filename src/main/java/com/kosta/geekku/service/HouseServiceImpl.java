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

import com.kosta.geekku.dto.HouseAnswerDto;
import com.kosta.geekku.dto.HouseDto;
import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.House;
import com.kosta.geekku.entity.HouseAnswer;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.repository.HouseAnswerRepository;
import com.kosta.geekku.repository.HouseDslRepository;
import com.kosta.geekku.repository.HouseRepository;
import com.kosta.geekku.repository.UserRepository;
import com.kosta.geekku.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HouseServiceImpl implements HouseService {

	private final HouseRepository houseRepository;
	private final HouseDslRepository houseDslRepository;
	private final UserRepository userRepository;
	private final HouseAnswerRepository houseAnswerRepository;
	private final CompanyRepository companyRepository;

	@Override
	public Integer houseWrite(HouseDto houseDto, UUID userId) throws Exception {
		House house = houseDto.toEntity();
		User user = userRepository.findById(userId).orElseThrow(() -> new Exception("일반회원 찾기 오류"));
		house.setUser(user);
		houseRepository.save(house);
		
		return house.getHouseNum();
	}

	@Transactional
	@Override
	public HouseDto houseDetail(Integer houseNum) throws Exception {
		House house = houseRepository.findById(houseNum).orElseThrow(() -> new Exception("집꾸 글번호 오류"));
		houseDslRepository.updateHouseViewCount(houseNum, house.getViewCount() + 1);
		return house.toDto();
	}

	@Override
	public List<HouseDto> houseList(PageInfo pageInfo, String type, String keyword) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);
		List<HouseDto> houseDtoList = null;
		Long allCnt = 0L;

		if (keyword == null || keyword.trim().equals("")) {
			houseDtoList = houseDslRepository.findHouseListByPaging(pageRequest).stream().map(h -> h.toDto())
					.collect(Collectors.toList());
			allCnt = houseDslRepository.findHouseCount();
		} else {
			houseDtoList = houseDslRepository.searchHouseListByPaging(pageRequest, type, keyword).stream()
					.map(h -> h.toDto()).collect(Collectors.toList());
			allCnt = houseDslRepository.searchHouseCount(type, keyword);
		}

		Integer allPage = (int) (Math.ceil(allCnt.doubleValue() / pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		pageInfo.setTotalCount(allCnt);
		return houseDtoList;
	}

	@Override
	public void houseDelete(Integer houseNum) throws Exception {
		houseRepository.findById(houseNum).orElseThrow(() -> new Exception("집꾸 글번호 오류"));
		houseRepository.deleteById(houseNum);
	}

	@Override
	public Integer houseAnswerWrite(HouseAnswerDto houseAnswerDto, UUID companyId) throws Exception {
		House house = houseRepository.findById(houseAnswerDto.getHouseNum()).orElseThrow(() -> new Exception("집꾸 글번호 오류"));
		Company company = companyRepository.findById(companyId).orElseThrow(() -> new Exception("기업회원 찾기 오류"));

		HouseAnswer houseAnswer = houseAnswerDto.toEntity();
		houseAnswer.setCompany(company);
		houseAnswerRepository.save(houseAnswer);
		
		return houseAnswer.getAnswerHouseNum();
	}

	@Transactional
	@Override
	public List<HouseAnswerDto> houseAnswerList(PageInfo pageInfo, Integer houseNum) throws Exception {
		House house = houseRepository.findById(houseNum).orElseThrow(() -> new Exception("집꾸 글번호 오류"));
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);

		List<HouseAnswerDto> houseAnswerDtoList = houseDslRepository.houseAnswerListByPaging(pageRequest, houseNum).stream()
				.map(a -> a.toDto()).collect(Collectors.toList());
		Long cnt = houseDslRepository.houseAnswerCount(houseNum);

		Integer allPage = (int) (Math.ceil(cnt.doubleValue() / pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		pageInfo.setTotalCount(cnt);
		
		return houseAnswerDtoList;
	}

	@Transactional
	@Override
	public void houseAnswerDelete(Integer houseAnswerNum, Integer houseNum) throws Exception {
		houseAnswerRepository.findById(houseAnswerNum).orElseThrow(() -> new Exception("집꾸 답변글 번호 오류"));
		houseAnswerRepository.deleteById(houseAnswerNum);
	}

	public Page<HouseAnswerDto> houseAnswerListForMypage(int page, int size, UUID companyId) {
		Optional<Company> company = companyRepository.findById(companyId);

		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<HouseAnswerDto> pageInfo = houseAnswerRepository.findAllByCompany(company, pageable)
				.map(HouseAnswer::toDto);

		return pageInfo;

	}

	@Override
	public Page<HouseDto> houseListForUserMypage(int page, int size, UUID userId) throws Exception {
		Optional<User> user = userRepository.findById(userId);

		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<HouseDto> pageInfo = houseRepository.findAllByUser(user, pageable).map(House::toDto);

		return pageInfo;
	}
}
