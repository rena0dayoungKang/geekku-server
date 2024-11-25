package com.kosta.geekku.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
	public Integer houseWrite(HouseDto houseDto) throws Exception {
		User user = userRepository.findById(houseDto.getUserId())
				.orElseThrow(() -> new RuntimeException("User not found"));
		House house = houseDto.toEntity(user);

		houseRepository.save(house);
		return house.getHouseNum();
	}

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

		return houseDtoList;
	}

	@Override
	public void houseDelete(Integer houseNum) throws Exception {
		houseRepository.findById(houseNum).orElseThrow(() -> new Exception("집꾸 글번호 오류"));
		houseRepository.deleteById(houseNum);
	}

	@Override
	public Integer houseAnswerWrite(HouseAnswerDto houseAnswerDto, Integer houseNum) throws Exception {
		House house = houseRepository.findById(houseNum).orElseThrow(() -> new Exception("집꾸 글번호 오류"));
		HouseAnswer houseAnswer = houseAnswerDto.toEntity();
		houseAnswerRepository.save(houseAnswer);
		return houseAnswer.getAnswerHouseNum();
	}

	@Transactional
	@Override
	public List<HouseAnswerDto> houseAnswerList(PageInfo pageInfo, Integer houseNum) throws Exception {
		House house = houseRepository.findById(houseNum).orElseThrow(() -> new Exception("집꾸 글번호 오류"));
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);

		List<HouseAnswerDto> houseAnswerDtoList = houseDslRepository.houseAnswerListByPaging(pageRequest).stream()
				.map(a -> a.toDto()).collect(Collectors.toList());
		Long cnt = houseDslRepository.houseAnswerCount();

		Integer allPage = (int) (Math.ceil(cnt.doubleValue() / pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);

		return houseAnswerDtoList;
	}

	@Transactional
	@Override
	public void houseAnswerDelete(Integer houseAnswerNum, Integer houseNum) throws Exception {
		houseAnswerRepository.findById(houseAnswerNum).orElseThrow(() -> new Exception("답변이 존재하지 않습니다."));
		houseAnswerRepository.deleteById(houseAnswerNum);
	}

	public Slice<HouseAnswerDto> houseAnswerListForMypage(int page, String companyId) {
		Optional<Company> company = companyRepository.findById(UUID.fromString(companyId));

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
		Slice<HouseAnswerDto> pageInfo = houseAnswerRepository.findAllByCompany(company, pageable)
				.map(HouseAnswer::toDto);

		return pageInfo;
	}
}
