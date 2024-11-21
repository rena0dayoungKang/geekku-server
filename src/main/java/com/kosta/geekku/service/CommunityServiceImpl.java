package com.kosta.geekku.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.CommunityDto;
import com.kosta.geekku.dto.CommunityFilterDto;
import com.kosta.geekku.entity.Community;
import com.kosta.geekku.repository.CommunityRepository;
import com.kosta.geekku.util.CommunitySpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

	private final CommunityRepository communityRepository;

	@Value("${upload.path}")
	private String uploadPath;

	@Override
	public Page<CommunityDto> getCommunityList(Pageable pageable) {
		return communityRepository.findAll(pageable).map(Community::toDto);
	}

	@Override
	public Integer createCommunity(CommunityDto communityDto) {
		Community community = communityDto.toEntity();
		communityRepository.save(community);
		return community.getCommunityNum();
	}

	@Override
	public CommunityDto getCommunityDetail(Integer communityNum) {
		return communityRepository.findById(communityNum)
				.orElseThrow(() -> new IllegalArgumentException("커뮤니티 조회에 실패했습니다.")).toDto();
	}

	@Override
	public Page<CommunityDto> getFilteredCommunityList(CommunityFilterDto filterDto, Pageable pageable) {
		Specification<Community> specification = CommunitySpecification.filterBy(filterDto);
		return communityRepository.findAll(specification, pageable).map(Community::toDto);
	}

	@Transactional
	@Override
	public void createCommunityWithCoverImage(String title, String content, String type, MultipartFile coverImage)
			throws Exception {
		Community community = Community.builder().title(title).content(content).type(type).build(); // 데이터 저장
		community = communityRepository.save(community);
		System.out.println("Community saved with ID: " + community.getCommunityNum());
		// 파일 저장
		if (coverImage == null || coverImage.isEmpty()) {
			throw new IllegalArgumentException("파일이 비어 있거나 업로드되지 않았습니다.");
		}
		try {
			File uploadDir = new File(uploadPath);// 디렉토리 확인 및 생성
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}
			String fileName = coverImage.getOriginalFilename();// 파일 저장 경로 생성
			String filePath = uploadPath + "/" + fileName;
			File dest = new File(filePath);// 파일 저장
			coverImage.transferTo(dest);
			community.setCoverImage(filePath);// 파일 경로를 엔티티에 업데이트
			communityRepository.save(community);
			System.out.println("파일 경로가 데이터베이스에 저장되었습니다: " + fileName);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("파일 저장 중 오류 발생: " + e.getMessage());
			throw new RuntimeException("파일 저장에 실패했습니다. 경로를 확인하세요.");
		}
	}

	@Override
    public void updateCommunity(Integer id, CommunityDto communityDto, MultipartFile coverImage) throws Exception {
        // 기존 데이터 조회
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new Exception("해당 커뮤니티 글을 찾을 수 없습니다."));

        // 데이터 업데이트
        community.setTitle(communityDto.getTitle());
        community.setContent(communityDto.getContent());
        community.setType(communityDto.getType());

        // 파일 처리 (새로운 파일이 있을 경우만)
        if (coverImage != null && !coverImage.isEmpty()) {
            // 기존 파일 삭제
            if (community.getCoverImage() != null) {
                File existingFile = new File(uploadPath, community.getCoverImage());
                if (existingFile.exists()) {
                    existingFile.delete();
                }
            }
            // 새로운 파일 저장
            String fileName = UUID.randomUUID() + "_" + coverImage.getOriginalFilename();
            String filePath = uploadPath + "/" + fileName;
            coverImage.transferTo(new File(filePath));
            // 파일 이름 업데이트
            community.setCoverImage(fileName);
        }

        // 변경 내용 저장
        communityRepository.save(community);
    }

}
