package com.kosta.geekku.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.kosta.geekku.dto.InteriorAllDto;
import com.kosta.geekku.entity.InteriorAllRequest;
import com.kosta.geekku.repository.InteriorAllRequestDslRepository;
import com.kosta.geekku.repository.InteriorAllRequestRepository;
import com.kosta.geekku.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InteriorAllRequestServiceImpl implements InteriorAllRequestService {

	private final InteriorAllRequestRepository interiorAllRepository;
	private final InteriorAllRequestDslRepository interiorAllRequestDslRepository;

	@Override
	public Integer interiorAllWrite(InteriorAllDto interiorAllDto) throws Exception {
		InteriorAllRequest interiorAll = interiorAllDto.toEntity();
		interiorAllRepository.save(interiorAll);
		return interiorAll.getRequestAllNum();
	}

	@Override
	public InteriorAllDto interiorDetail(Integer num) throws Exception {
		InteriorAllRequest interiorAll = interiorAllRepository.findById(num).orElseThrow(() -> new Exception("글번호 오류"));
		System.out.println("service" + num);
		// interiorAllDslRepository.updateInteriorAllViewCount(num,
		// interiorAll.getViewCount() + 1);
		return interiorAll.toDto();
	}

	@Override
	public Integer interiorAllModify(InteriorAllDto interiorAllDto) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<InteriorAllDto> interiorAllList(PageInfo pageInfo, String type, String word) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);
		List<InteriorAllDto> interiorAllDtoList = null;
		Long allCnt = 0L;
		if (word == null || word.trim().equals("")) { // 전체 목록
			interiorAllDtoList = interiorAllRequestDslRepository.findInteriorAllListByPaging(pageRequest).stream()
					.map(b -> b.toDto()).collect(Collectors.toList());
			System.out.println(interiorAllDtoList);
			allCnt = interiorAllRequestDslRepository.findInteriorAllCount();
		} else { // 검색
			interiorAllDtoList = interiorAllRequestDslRepository.searchInteriorAllListByPaging(pageRequest, type, word)
					.stream().map(b -> b.toDto()).collect(Collectors.toList());
			allCnt = interiorAllRequestDslRepository.searchInteriorAllCount(type, word);
		}

		Integer allPage = (int) (Math.ceil(allCnt.doubleValue() / pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		return interiorAllDtoList;
	}

}
