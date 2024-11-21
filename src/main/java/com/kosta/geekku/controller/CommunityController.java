package com.kosta.geekku.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosta.geekku.dto.CommunityDto;
import com.kosta.geekku.dto.CommunityFilterDto;
import com.kosta.geekku.service.CommunityService;

@RestController
public class CommunityController {

	@Autowired
	private CommunityService communityService;

	// 페이징된 커뮤니티 리스트 조회
	@GetMapping("/test1")
	public ResponseEntity<Page<CommunityDto>> getCommunityList(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<CommunityDto> communityList = communityService.getCommunityList(pageable);
		return ResponseEntity.ok(communityList);
	}

	// 커뮤니티 글 작성
	@PostMapping("/test2")
	public ResponseEntity<String> createCommunity(@RequestBody CommunityDto communityDto) {
		Integer communityId = communityService.createCommunity(communityDto);
		return new ResponseEntity<>("Created Community ID: " + communityId, HttpStatus.CREATED);
	}

	// 커뮤니티 글 상세 조회
	@GetMapping("/test3/{num}")
	public ResponseEntity<CommunityDto> getCommunityDetail(@PathVariable Integer num) {
		CommunityDto communityDetail = communityService.getCommunityDetail(num);
		return new ResponseEntity<>(communityDetail, HttpStatus.OK);
	}

	// 필터링 조회
	@PostMapping("/test4")
	public ResponseEntity<Page<CommunityDto>> getFilteredCommunities(@RequestBody CommunityFilterDto filterDto,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<CommunityDto> filteredList = communityService.getFilteredCommunityList(filterDto, pageable);
		return ResponseEntity.ok(filteredList);
	}
	
	// 커뮤니티 글 작성 글자 포함(2번이랑 둘 중에 하나만 쓸 거임)
	@PostMapping("/test5")
    public ResponseEntity<String> createCommunity(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("type") String type,
            @RequestParam(value = "coverImage", required = false) MultipartFile coverImage) {

        try {
            communityService.createCommunityWithCoverImage(title, content, type, coverImage);
            return new ResponseEntity<>("커뮤니티 생성에 성공했습니다", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("커뮤니티 생성에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	//커뮤니티 글 수정
	@PutMapping(value = "/test6/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateCommunity(
	        @PathVariable Integer id,
	        @RequestPart("community") String communityJson,
	        @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
	    try {
	        // JSON 데이터를 DTO로 변환
	        ObjectMapper objectMapper = new ObjectMapper();
	        CommunityDto communityDto = objectMapper.readValue(communityJson, CommunityDto.class);

	        // 서비스 호출
	        communityService.updateCommunity(id, communityDto, coverImage);

	        return ResponseEntity.ok("수정 완료");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 실패");
	    }
	}

}