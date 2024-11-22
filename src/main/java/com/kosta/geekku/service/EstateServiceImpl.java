package com.kosta.geekku.service;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.EstateDto;
import com.kosta.geekku.entity.Estate;
import com.kosta.geekku.entity.EstateBookmark;
import com.kosta.geekku.entity.EstateImage;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.repository.EstateBookmarkRepository;
import com.kosta.geekku.repository.EstateDslRepository;
import com.kosta.geekku.repository.EstateImageRepository;
import com.kosta.geekku.repository.EstateRepository;
import com.kosta.geekku.repository.UserRepository;
import com.kosta.geekku.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstateServiceImpl implements EstateService {
	
	private final EstateRepository estateRepository;
	private final EstateImageRepository estateImageRepository;
	private final EstateBookmarkRepository estateBookmarkRepository;
	private final EstateDslRepository estateDslRepository;
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Override
	public Integer estateWrite(EstateDto estateDto, List<MultipartFile> estateImageList) throws Exception {
		Estate estate = estateDto.toEntity();
		estateRepository.save(estate);
		
		if (estateImageList != null && estateImageList.size() > 0) {
			for (MultipartFile file: estateImageList) {
				EstateImage estateImage = new EstateImage();
				estateImage.setDirectory(uploadPath);
				estateImage.setName(file.getOriginalFilename());
				estateImage.setSize(file.getSize());
				estateImage.setContentType(file.getContentType());
				estateImage.setEstate(estate);
				estateImageRepository.save(estateImage);
				
				File upFile = new File(uploadPath, estateImage.getEstateImageNum() + "");
				file.transferTo(upFile);
			}
		}
		
		return estate.getEstateNum();
	}

	@Override
	public EstateDto estateDetail(Integer estateNum) throws Exception {
		Estate estate = estateRepository.findById(estateNum).orElseThrow(() -> new Exception("매물번호 오류"));
		return estate.toDto();
	}
	
	@Override
	public List<EstateDto> estateList(PageInfo pageInfo, String type, String keyword) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);
		List<EstateDto> estateDtoList = null;
		Long allCnt = 0L;
		
		if (type == null && (keyword == null || keyword.trim().equals(""))) { //전체목록
			estateDtoList = estateDslRepository.findEstateListByPaging(pageRequest).stream()
								.map(e -> e.toDto()).collect(Collectors.toList());
			allCnt = estateDslRepository.findEstateCount();
		} else if (type != null && (keyword == null || keyword.trim().equals(""))) { //타입만 선택
			estateDtoList = estateDslRepository.typeEstateListByPaging(pageRequest, type).stream()
								.map(e -> e.toDto()).collect(Collectors.toList());
			allCnt = estateDslRepository.typeEstateCount(type);
		} else { //타입선택 + 검색
			estateDtoList = estateDslRepository.searchEstateListByPaging(pageRequest, type, keyword).stream()
								.map(e -> e.toDto()).collect(Collectors.toList());
			allCnt = estateDslRepository.searchEstateCount(type, keyword);
		}
		
		Integer allPage = (int)(Math.ceil(allCnt.doubleValue() / pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);
		
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		
		return estateDtoList;
	}
	

	@Override
	public List<EstateDto> estateListForMain() throws Exception {
		List<EstateDto> estateDtoList = estateDslRepository.findEstateListForMain().stream()
											.map(e -> e.toDto()).collect(Collectors.toList());
		return estateDtoList;
	}
	
	@Override
	public void estateDelete(Integer estateNum) throws Exception {
		estateRepository.deleteById(estateNum);
	}

	@Override
	public Integer checkBookmark(String userId, Integer estateNum) throws Exception {
		EstateBookmark estateBookmark = estateBookmarkRepository.findByEstateNumAndUserId(estateNum, UUID.fromString(userId));
		return estateBookmark.getBookmarkEstateNum();
	}

	@Override
	public boolean toggleBookmark(String userId, Integer estateNum) throws Exception {
		EstateBookmark estateBookmark = estateBookmarkRepository.findByEstateNumAndUserId(estateNum, UUID.fromString(userId));
		
		if (estateBookmark == null) {
			estateBookmarkRepository.save(EstateBookmark.builder().userId(UUID.fromString(userId)).estateNum(estateNum).build());
			return true;
		} else {
			estateBookmarkRepository.deleteById(estateBookmark.getBookmarkEstateNum());
			return false;
		}
	}

	@Override
	public List<EstateDto> estateListForMypage(PageInfo pageInfo, String companyId) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);
		List<EstateDto> estateDtoList = null;
		Long allCnt = 0L;
		
		estateDtoList = estateDslRepository.findMypageEstateListByPaging(pageRequest, UUID.fromString(companyId)).stream()
				.map(e -> e.toDto()).collect(Collectors.toList());
		allCnt = estateDslRepository.findMypageEstateCount(UUID.fromString(companyId));
	
		Integer allPage = (int)(Math.ceil(allCnt.doubleValue() / pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10 + 1;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);
		
		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);
		
		return estateDtoList;
	}
}
