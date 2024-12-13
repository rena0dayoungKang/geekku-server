package com.kosta.geekku.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.kosta.geekku.config.auth.PrincipalDetails;
import com.kosta.geekku.dto.CommunityCommentDto;
import com.kosta.geekku.dto.CommunityDto;
import com.kosta.geekku.dto.CommunityFilterDto;
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

	/** @deprecated getCommunityDetail에서 카운트하게 수정함 */
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
			} else {
				res.put("bookmark", false);
			}

			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/communityCall/{num}")
	public ResponseEntity<Map<String, Object>> communityCall(@PathVariable Integer num) {
		try {
			Map<String, Object> res = new HashMap<>();
			CommunityDto communityDetail = communityService.getCommunityDetail(num);
			res.put("communityDetail", communityDetail);
			return new ResponseEntity<>(res, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	// 필터링 조회
	@PostMapping("/communityListFiltered")
	public ResponseEntity<Page<CommunityDto>> getFilteredCommunities(@RequestBody CommunityFilterDto filterDto,
			@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "size", defaultValue = "10") int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<CommunityDto> filteredList = communityService.getFilteredCommunityList(filterDto, pageable);
		return ResponseEntity.ok(filteredList);
	}

	// 커뮤니티 글 작성
	@PostMapping("/user/communityCreate")
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
	@PutMapping(value = "/user/communityUpdate/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?> updateCommunity(@PathVariable Integer id, CommunityDto communityDto,
			@RequestPart(value = "coverImage", required = false) MultipartFile file) {
		try {
			communityService.updateCommunity(id, communityDto, file);
			return ResponseEntity.ok("수정 완료");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("수정 실패");
		}
	}

	// 커뮤니티 북마크
	@PostMapping("/user/communityBookmark") // 예시
	public ResponseEntity<String> toggleCommunityBookmark(Authentication authentication,
			@RequestParam Integer communityNum) {
		try {
			String userId = ((PrincipalDetails) authentication.getPrincipal()).getUser().getUserId().toString();
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
//	@PostMapping("/user/communityCommentWrite")
//	public ResponseEntity<String> createComment(@RequestParam("communityId") Integer communityId,
//			@RequestParam("userId") String userId, @RequestParam("content") String content) {
//
//		try {
//			communityService.createComment(communityId, userId, content);
//			return new ResponseEntity<>("댓글 작성에 성공했습니다.", HttpStatus.CREATED);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return new ResponseEntity<>("댓글 작성에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

	// 커뮤니티 댓글 작성 후 전체 댓글 반환
	// 커뮤니티 댓글 작성 후 전체 댓글 반환
	@PostMapping("/user/communityCommentWrite")
	public ResponseEntity<?> createCommentAndFetchAll(@RequestParam("communityId") Integer communityId,
			@RequestParam("userId") String userId, @RequestParam("content") String content) {

		try {
			// 댓글 작성 및 전체 댓글 리스트 반환
			List<CommunityCommentDto> comments = communityService.createComment(communityId, userId, content);
			return new ResponseEntity<>(comments, HttpStatus.CREATED); // 전체 댓글 리스트 반환
		} catch (Exception e) {
			e.printStackTrace();
			// 실패 시 빈 배열 반환
			return new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR);
			// 또는 에러 객체를 반환하려면 아래와 같이 할 수 있습니다:
			// return new ResponseEntity<>(Map.of("error", "댓글 작성에 실패했습니다."),
			// HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 커뮤니티 댓글 삭제
	@DeleteMapping("/user/commentDelete/{commentNum}")
	public ResponseEntity<String> deleteComment(@PathVariable Integer commentNum) {
		try {
			communityService.deleteComment(commentNum);
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

	/** @deprecated 내가 쓴 커뮤니티 글 조회 */
	/*
	 * @GetMapping("/test12/{userId}") public ResponseEntity<Page<CommunityDto>>
	 * getCommunityListByUserId(
	 * 
	 * @PathVariable String userId,
	 * 
	 * @RequestParam(value = "page", defaultValue = "0") int page,
	 * 
	 * @RequestParam(value = "size", defaultValue = "10") int size) { try { Pageable
	 * pageable = PageRequest.of(page, size); // 페이징 처리 Page<CommunityDto>
	 * communityPosts = communityService.getPostsByUserId(userId, pageable); return
	 * ResponseEntity.ok(communityPosts); } catch (Exception e) {
	 * e.printStackTrace(); return
	 * ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); } }
	 */

	// 커뮤니티 이미지 조회
	@GetMapping("/communityImage/{imageName}")
	public void getImage(@PathVariable String imageName, HttpServletResponse response) {
		try {
			String Path = uploadPath + "communityImage/";
			File file = new File(Path, imageName);
			if (!file.exists()) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}
			InputStream ins = new FileInputStream(file);
			response.setContentType("image/png");
			FileCopyUtils.copy(ins, response.getOutputStream());
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 서버 오류
		}
	}

	// 커뮤니티 글 삭제
	@DeleteMapping("/user/communityDelete/{communityNum}")
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