package com.kosta.geekku.service;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.CommunityDto;
import com.kosta.geekku.entity.CFile;
import com.kosta.geekku.entity.Community;
import com.kosta.geekku.repository.CommunityRepository;
import com.kosta.geekku.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

    @Value("${upload.path}")
    private String uploadPath;

    private final CommunityRepository communityRepository;
    private final FileRepository fileRepository;

    @Override
    public Integer createCommunity(CommunityDto communityDto, List<MultipartFile> fileList) throws Exception {
        // DTO를 Entity로 변환
        Community community = communityDto.toEntity();
        communityRepository.save(community);

        // 파일 저장 로직
        if (fileList != null && !fileList.isEmpty()) {
            for (MultipartFile file : fileList) {
                CFile cFile = new CFile();
                cFile.setDirectory(uploadPath);
                cFile.setName(file.getOriginalFilename());
                cFile.setContentType(file.getContentType());
                cFile.setSize(file.getSize());
                cFile.setCommunity(community);
                fileRepository.save(cFile);

                File upFile = new File(uploadPath, cFile.getId().toString());
                file.transferTo(upFile);
            }
        }

        return community.getCommunityNum();
    }

    @Override
    public CommunityDto getCommunityDetail(Integer communityNum) throws Exception {
        return communityRepository.findById(communityNum)
                .orElseThrow(() -> new Exception("해당 커뮤니티 글을 찾을 수 없습니다."))
                .toDto();
    }

    @Override
    public List<CommunityDto> getCommunityList() {
        return communityRepository.findAll().stream()
                .map(Community::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCommunity(Integer communityNum) throws Exception {
        communityRepository.deleteById(communityNum);
    }
}
