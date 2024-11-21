package com.kosta.geekku.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.entity.InteriorSample;
import com.kosta.geekku.repository.InteriorDslRepository;
import com.kosta.geekku.repository.InteriorRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InteriorSeviceImpl implements InteriorService {
	
	private final InteriorRepository interiorRepository;
	private final InteriorDslRepository interiorDslRepository;
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Override
	public List<InteriorDto> interiorListForMain() throws Exception {
		List<InteriorDto> interiorDtoList = null;
		interiorDtoList = interiorDslRepository.findInteriorListForMain()
				.stream().map(i->i.toDto()).collect(Collectors.toList());
		return interiorDtoList;
	}

	@Override
	public List<InteriorSample> sampleListForMain() throws Exception {
		List<InteriorSample> sampleList = null;
		sampleList = interiorDslRepository.findSampleListForMain();
		return sampleList;
	}
}
