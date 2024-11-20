package com.kosta.geekku.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.EstateDto;
import com.kosta.geekku.entity.Estate;
import com.kosta.geekku.entity.EstateImage;
import com.kosta.geekku.repository.EstateImageRepository;
import com.kosta.geekku.repository.EstateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EstateServiceImpl implements EstateService {
	
	private final EstateRepository estateRepository;
	private final EstateImageRepository estateImageRepository;
	
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
	
}
