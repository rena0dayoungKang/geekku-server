package com.kosta.geekku.controller;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.kosta.geekku.entity.Community;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.service.CommunityService;

@RestController
public class CommunityController {

	@Autowired
	private CommunityService communityService;

	// 페이징된 커뮤니티 리스트 조회
	@GetMapping("/communityList") // 예시 http://localhost:8080/test1?page=0&size=3
	public ResponseEntity<Page<CommunityDto>> getCommunityList(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<CommunityDto> communityList = communityService.getCommunityList(pageable);
		return ResponseEntity.ok(communityList);
	}

	// 커뮤니티 글 작성
	@PostMapping("/test2") // 예시 http://localhost:8080/test2/ + json 조건
	public ResponseEntity<String> createCommunity(@RequestBody CommunityDto communityDto) {
		Integer communityId = communityService.createCommunity(communityDto);
		return new ResponseEntity<>("커뮤니티 생성: " + communityId, HttpStatus.CREATED);
	}

	// 커뮤니티 글 상세 조회
	@GetMapping("/test3/{num}") // 예시 http://localhost:8080/test3/1
	public ResponseEntity<CommunityDto> getCommunityDetail(@PathVariable Integer num) {
		CommunityDto communityDetail = communityService.getCommunityDetail(num);
		return new ResponseEntity<>(communityDetail, HttpStatus.OK);
	}

	// 필터링 조회
	@PostMapping("/test4") // 예시 http://localhost:8080/test4/ + json 조건
	public ResponseEntity<Page<CommunityDto>> getFilteredCommunities(@RequestBody CommunityFilterDto filterDto,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<CommunityDto> filteredList = communityService.getFilteredCommunityList(filterDto, pageable);
		return ResponseEntity.ok(filteredList);
	}

	// 커뮤니티 글 작성 글자 포함(2번이랑 둘 중에 하나만 쓸 거임 /user 형태로 바꿔야함 나중에)
	@PostMapping("/communityCreate")
	public ResponseEntity<String> createCommunity(@RequestParam("title") String title,
	        @RequestParam("content") String content, 
	        @RequestParam("type") String type,
	        @RequestParam("userId") String userId,  // userId 파라미터 추가
	        @RequestParam("address1") String address1,
	        @RequestParam("address2") String address2,
	        @RequestParam("familyType") String familyType,
	        @RequestParam("interiorType") String interiorType,
	        @RequestParam("money") Integer money,
	        @RequestParam("periodStartDate") Date periodStartDate,
	        @RequestParam("periodEndDate") Date periodEndDate,
	        @RequestParam("size") Integer size,
	        @RequestParam("style") String style,
	        @RequestParam(value = "coverImage", required = false) MultipartFile coverImage) {
	    try {
	        communityService.createCommunityWithCoverImage(title, content, type, coverImage, userId, address1, address2, familyType,
	        		interiorType, money, periodStartDate, periodEndDate, size, style);  // userId를 서비스 메서드에 전달
	        return new ResponseEntity<>("커뮤니티 생성에 성공했습니다", HttpStatus.CREATED);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>("커뮤니티 생성에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	// 커뮤니티 글 수정
	@PutMapping(value = "/user/test6/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateCommunity(@PathVariable Integer id, @RequestPart("community") String communityJson,
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

	// 커뮤니티 북마크
	@PostMapping("/user/test7") // 예시
								// http://localhost:8080/test7?userId=7e7506d5-b944-40c8-a269-c3c58d2067bb&communityNum=3
	public ResponseEntity<String> toggleCommunityBookmark(@RequestParam String userId,
			@RequestParam Integer communityNum) {
		try {

			boolean isBookmarked = communityService.toggleCommunityBookmark(userId, communityNum);
			if (isBookmarked) {
				return ResponseEntity.ok("북마크가 활성화되었습니다.");
			} else {
				return ResponseEntity.ok("북마크가 비활성화되었습니다.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body("서버 오류가 발생했습니다. 오류: " + e.getMessage());
		}
	}

	// 커뮤니티 댓글 작성
	@PostMapping("/user/test8") // 예시
								// http://localhost:8080/test8?communityId=3&userId=7e7506d5-b944-40c8-a269-c3c58d2067bb&content="댓글
								// 내용"
	public ResponseEntity<String> createComment(@RequestParam("communityId") Integer communityId,
			@RequestParam("userId") String userId, @RequestParam("content") String content) {

		try {
			communityService.createComment(communityId, userId, content);
			return new ResponseEntity<>("댓글 작성에 성공했습니다.", HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("댓글 작성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 커뮤니티 댓글 삭제
	@DeleteMapping("/user/test9/{commentId}") // 예시 http://localhost:8080/test9/1
	public ResponseEntity<String> deleteComment(@PathVariable Integer commentId) {
		try {
			communityService.deleteComment(commentId);
			return new ResponseEntity<>("댓글이 성공적으로 삭제되었습니다", HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("댓글 삭제에 실패했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 커뮤니티 프로필 조회를 위한 개인 정보 조회(아이디 닉네임 이메일)
	@GetMapping("/test10/{userId}") // 예시 http://localhost:8080/test10/7e7506d5-b944-40c8-a269-c3c58d2067bb
	public ResponseEntity<?> getUserProfile(@PathVariable String userId) {
		try {
			User user = communityService.getUserProfile(userId);
			return ResponseEntity.ok(user);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("유저 정보를 찾을 수 없습니다.");
		}
	}

	// 해당 유저가 작성한 커뮤니티 게시글을 가져오는 API
	@GetMapping("/test11/{userId}") // 예시 http://localhost:8080/test11/7e7506d5-b944-40c8-a269-c3c58d2067bb
	public ResponseEntity<?> getUserCommunities(@PathVariable String userId) {
		try {
			List<Community> communities = communityService.getUserCommunities(userId);
			return ResponseEntity.ok(communities);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("커뮤니티 게시글을 찾을 수 없습니다.");
		}
	}

	// 내가 쓴 커뮤니티 글 조회
//	@GetMapping("/test12/{userId}")
//	public ResponseEntity<Page<CommunityDto>> getCommunityListByUserId(
//			 @PathVariable String userId,
//		        @RequestParam(value = "page", defaultValue = "0") int page,
//		        @RequestParam(value = "size", defaultValue = "10") int size) {
//		    try {
//		        Pageable pageable = PageRequest.of(page, size); // 페이징 처리
//		        Page<CommunityDto> communityPosts = communityService.getPostsByUserId(userId, pageable);
//		        return ResponseEntity.ok(communityPosts);
//		    } catch (Exception e) {
//		        e.printStackTrace();
//		        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//		    }
//		}
	
}