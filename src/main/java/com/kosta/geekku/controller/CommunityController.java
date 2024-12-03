package com.kosta.geekku.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.FileCopyUtils;
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
import com.kosta.geekku.config.auth.PrincipalDetails;
import com.kosta.geekku.dto.CommunityCommentDto;
import com.kosta.geekku.dto.CommunityDto;
import com.kosta.geekku.dto.CommunityFilterDto;
import com.kosta.geekku.dto.UserDto;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.CommunityRepository;
import com.kosta.geekku.service.CommunityService;

@RestController
public class CommunityController {

	@Value("${upload.path}")
	private String uploadPath;

	@Autowired
	private CommunityService communityService;
	@Autowired
	private CommunityRepository communityRepository;

	// 커뮤니티 리스트 조회
	@GetMapping("/communityList")
	public ResponseEntity<Page<CommunityDto>> getCommunityList(
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<CommunityDto> communityList = communityService.getCommunityList(pageable);
		return ResponseEntity.ok(communityList);
	}

	// 게시글 조회수 조회
	@GetMapping("/communityList/count")
	public ResponseEntity<Map<String, Long>> getCommunityListCount() {
		long totalCount = communityRepository.count(); // 커뮤니티 테이블의 총 개수
		Map<String, Long> response = new HashMap<>();
		response.put("totalElements", totalCount);
		return ResponseEntity.ok(response);
	}

	// 커뮤니티 조회수 증가
	@PostMapping("/increaseViewCount/{communityNum}")
	public ResponseEntity<?> increaseViewCount(@PathVariable Integer communityNum) {
		try {
			communityService.increaseViewCount(communityNum);
			return ResponseEntity.ok("조회수가 증가되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("조회수 증가 중 오류가 발생했습니다.");
		}
	}

	// 커뮤니티 글 상세 조회
	@PostMapping("/communityDetail/{num}")
	public ResponseEntity<Map<String, Object>> communityDetail(@PathVariable Integer num,
			@RequestBody Map<String, String> param) {
		try {
			Map<String, Object> res = new HashMap<>();
			CommunityDto communityDetail = communityService.getCommunityDetail(num);
			res.put("communityDetail", communityDetail);

			List<CommunityCommentDto> commentList = communityService.getCommentsByCommunityId(num);
			res.put("commentList", commentList);

			if (param.get("userId") != null) {
				Boolean bookmark = communityService.getCommunityBookmark(param.get("userId"), num);
				res.put("bookmark", bookmark);
			}

			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// 필터링 조회
	@PostMapping("/communityList2") // 예시 http://localhost:8080/communityList2/ + json 조건
	public ResponseEntity<Page<CommunityDto>> getFilteredCommunities(@RequestBody CommunityFilterDto filterDto,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<CommunityDto> filteredList = communityService.getFilteredCommunityList(filterDto, pageable);
		return ResponseEntity.ok(filteredList);
	}

	// 커뮤니티 글 작성
	@PostMapping("/communityCreate")
	public ResponseEntity<Integer> createCommunity(@RequestParam("title") String title,
			@RequestParam("content") String content, @RequestParam("type") String type,
			@RequestParam("userId") String userId, // userId 파라미터 추가
			@RequestParam("address1") String address1, @RequestParam("address2") String address2,
			@RequestParam("familyType") String familyType, @RequestParam("interiorType") String interiorType,
			@RequestParam("money") Integer money, @RequestParam("periodStartDate") Date periodStartDate,
			@RequestParam("periodEndDate") Date periodEndDate, @RequestParam("size") Integer size,
			@RequestParam("style") String style,
			@RequestParam(value = "coverImage", required = false) MultipartFile coverImage) {
		try {
			Integer communityNum = communityService.createCommunityWithCoverImage(title, content, type, coverImage,
					userId, address1, address2, familyType, interiorType, money, periodStartDate, periodEndDate, size,
					style); // userId를 서비스 메서드에 전달
			return new ResponseEntity<>(communityNum, HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
	@PostMapping("/communityBookmark") // 예시
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

	// 댓글 조회 (특정 커뮤니티의 댓글 목록)
	@GetMapping("/communityComment/{communityNum}")
	public ResponseEntity<List<CommunityCommentDto>> getCommunityComments(@PathVariable Integer communityNum) {
		try {
			List<CommunityCommentDto> comments = communityService.getCommentsByCommunityId(communityNum);
			return new ResponseEntity<>(comments, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 커뮤니티 댓글 작성
	@PostMapping("/communityCommentWrite")
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
	@DeleteMapping("/user/test9/{commentId}")
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
	@GetMapping("/personProfile/{userId}")
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
	@GetMapping("/personCommunities/{userId}")
	public ResponseEntity<?> getUserCommunities(@PathVariable String userId) {
		try {
			List<CommunityDto> communities = communityService.getUserCommunities(userId);
			return ResponseEntity.ok(communities); // DTO 반환
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
	
	// 로그인한 유저의 커뮤니티 게시글 가져오기
	@GetMapping("/myCommunities")
	public ResponseEntity<?> getMyCommunities(Authentication authentication) {
		if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인 필요");
		}
		if (!(authentication.getPrincipal() instanceof PrincipalDetails)) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 객체가 올바르지 않습니다.");
		}
		try {
			PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
			if (principal.getUser() == null || principal.getUser().getUserId() == null) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 정보가 누락되었습니다.");
			}
			UUID userId = principal.getUser().getUserId();
			System.out.println("User ID: " + userId);
			List<CommunityDto> communities = communityService.getUserCommunities(userId.toString());
			return ResponseEntity.ok(communities);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
		}
	}

	// 커뮤니티 이미지 조회
	@GetMapping("/communityImage/{imageName}")
	public void getImage(@PathVariable String imageName, HttpServletResponse response) {
		try {
			String uploadPath = "C:/geekku/image_upload/communityImage/";
			File file = new File(uploadPath, imageName);
			if (!file.exists()) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			InputStream ins = new FileInputStream(file);
			response.setContentType("image/png"); // 이미지 MIME 타입 설정 (필요시 이미지 형식 변경)
			FileCopyUtils.copy(ins, response.getOutputStream()); // 파일을 OutputStream으로 복사
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 서버 오류
		}
	}

	// 커뮤니티 글 삭제
	@DeleteMapping("/communityDelete/{communityNum}")
	public ResponseEntity<?> deleteCommunity(@PathVariable Integer communityNum) {
		try {
			communityService.deleteCommunity(communityNum); // 삭제 로직 호출
			return ResponseEntity.ok("커뮤니티 게시글이 삭제되었습니다.");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류가 발생했습니다.");
		}
	}

}