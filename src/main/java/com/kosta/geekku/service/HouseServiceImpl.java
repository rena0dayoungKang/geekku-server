package com.kosta.geekku.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kosta.geekku.dto.HouseDto;
import com.kosta.geekku.entity.House;
import com.kosta.geekku.entity.User;
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
	
	@Override
	public Integer houseWrite(HouseDto houseDto) throws Exception {
		User user = userRepository.findById(houseDto.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
		House house = houseDto.toEntity(user);
		
		houseRepository.save(house);
		return house.getHouseNum();
	}

	@Override
	public HouseDto houseDetail(Integer houseNum) throws Exception {
		House house = houseRepository.findById(houseNum).orElseThrow(() -> new Exception("집꾸 글번호 오류"));
		return house.toDto();
	}

	@Override
	public List<HouseDto> houseList(PageInfo pageInfo, String type, String keyword) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);
		List<HouseDto> houseDtoList = null;
		Long allCnt = 0L;
		
		if (keyword == null || keyword.trim().equals("")) {
			houseDtoList = houseDslRepository.findHouseListByPaging(pageRequest).stream()
								.map(h -> h.toDto()).collect(Collectors.toList());
			allCnt = houseDslRepository.findHouseCount();
		} else {
			houseDtoList = houseDslRepository.searchHouseListByPaging(pageRequest, type, keyword).stream()
								.map(h -> h.toDto()).collect(Collectors.toList());
			allCnt = houseDslRepository.searchHouseCount(type, keyword);
		}
		
		Integer allPage = (int)(Math.ceil(allCnt.doubleValue() / pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);
		
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		
		return houseDtoList;
	}

	@Override
	public void houseDelete(Integer houseNum) throws Exception {
		houseRepository.deleteById(houseNum);
	}

}
