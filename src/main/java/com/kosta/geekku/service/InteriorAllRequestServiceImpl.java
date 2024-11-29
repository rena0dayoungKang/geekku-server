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
import com.kosta.geekku.dto.InteriorAllDto;
import com.kosta.geekku.dto.InteriorAnswerDto;
import com.kosta.geekku.dto.OnestopAnswerDto;
import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.InteriorAllAnswer;
import com.kosta.geekku.entity.InteriorAllRequest;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.entity.OnestopAnswer;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.repository.InteriorAllAnswerRepository;
import com.kosta.geekku.repository.InteriorAllRequestDslRepository;
import com.kosta.geekku.repository.InteriorAllRequestRepository;
import com.kosta.geekku.repository.UserRepository;
import com.kosta.geekku.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InteriorAllRequestServiceImpl implements InteriorAllRequestService {

	private final InteriorAllRequestRepository interiorAllRepository;
	private final InteriorAllRequestDslRepository interiorAllRequestDslRepository;
	private final InteriorAllAnswerRepository interiorAllAnswerRepository;
	private final UserRepository userRepository;
	private final CompanyRepository companyRepository;

	@Override
	public Integer interiorAllWrite(InteriorAllDto interiorAllDto) throws Exception {
		
		InteriorAllRequest interiorAll = interiorAllDto.toEntity();
		interiorAllRepository.save(interiorAll);
		return interiorAll.getRequestAllNum();
	}

	@Override
	public InteriorAllDto interiorDetail(Integer interiorNum) throws Exception {
		InteriorAllRequest interiorAll = interiorAllRepository.findById(interiorNum).orElseThrow(() -> new Exception("글번호 오류"));
		interiorAllRequestDslRepository.updateOnestopViewCount(interiorNum, interiorAll.getViewCount() + 1);
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
		if (word == null || word.trim().equals("")) { // �쟾泥� 紐⑸줉
			interiorAllDtoList = interiorAllRequestDslRepository.findInteriorAllListByPaging(pageRequest).stream()
					.map(b -> b.toDto()).collect(Collectors.toList());
			System.out.println(interiorAllDtoList);
			allCnt = interiorAllRequestDslRepository.findInteriorAllCount();
		} else { // 寃��깋
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

	@Override
	@Transactional
	public void interiorAllDelete(Integer num) throws Exception {
		interiorAllRepository.deleteById(num);

	}

	@Override
	public Integer interiorAnswerWrite(InteriorAnswerDto interiorAnswerDto, Integer requestAllNum) throws Exception {
		InteriorAllRequest interiorAllRequest = interiorAllRepository.findById(requestAllNum)
				.orElseThrow(() -> new Exception("諛⑷씀 湲�踰덊샇 �삤瑜�"));
		InteriorAllAnswer interiorAllAnswer = interiorAnswerDto.toEntity();
		interiorAllAnswerRepository.save(interiorAllAnswer);
		return interiorAllAnswer.getAnswerAllNum();
	}

	@Override
	public List<InteriorAnswerDto> interiorAnswerList(PageInfo pageInfo, Integer requestAllNum) throws Exception {
		InteriorAllRequest interiorAll = interiorAllRepository.findById(requestAllNum)
				.orElseThrow(() -> new Exception("諛⑷씀 湲�踰덊샇 �삤瑜�"));
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);

		List<InteriorAnswerDto> interiorAnswerDtoList = interiorAllRequestDslRepository
				.interiorAllAnswerListByPaging(pageRequest).stream().map(a -> a.toDto()).collect(Collectors.toList());
		Long cnt = interiorAllRequestDslRepository.interiorAllAnswerCount();

		Integer allPage = (int) (Math.ceil(cnt.doubleValue() / pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);

		return interiorAnswerDtoList;
	}

	@Transactional
	@Override
	public void interiorAnswerDelete(Integer answerAllNum, Integer requestAllNum) throws Exception {
		interiorAllAnswerRepository.findById(answerAllNum).orElseThrow(() -> new Exception("�떟蹂��씠 議댁옱�븯吏� �븡�뒿�땲�떎."));
		System.out.println(answerAllNum);
		interiorAllAnswerRepository.deleteById(answerAllNum);

	}

	/*
	 * @Override public Page<InteriorAllDto> interiorAllListForUserMypage(int page,
	 * int size, String userId) throws Exception { Optional<User> user =
	 * userRepository.findById(UUID.fromString(userId));
	 * 
	 * Pageable pageable = PageRequest.of(page - 1, size,
	 * Sort.by(Sort.Direction.DESC, "createAt")); Page<InteriorAllDto> pageInfo =
	 * interiorAllRequestDslRepository.findAllByUser(user, pageable)
	 * .map(InteriorAllRequest::toDto); }
	 */

	@Override
	public Slice<InteriorAnswerDto> interiorAnswerListForMypage(Integer page, String companyId) throws Exception {

		Optional<Company> company = companyRepository.findById(UUID.fromString(companyId));

		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
		Slice<InteriorAnswerDto> pageInfo = interiorAllAnswerRepository.findAllByCompany(company, pageable)
				.map(InteriorAllAnswer::toDto);

		return pageInfo;
	}

	@Override
	public Page<InteriorAllDto> interiorAllListForUserMypage(int page, int size, String userId) throws Exception {
		Optional<User> user = userRepository.findById(UUID.fromString(userId));
		
		Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(Sort.Direction.DESC, "createAt"));
		Page<InteriorAllDto> pageInfo = interiorAllRepository.findAllByUser(user, pageable).map(InteriorAllRequest::toDto);
		
		return pageInfo;
	}

}
