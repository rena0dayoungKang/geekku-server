package com.kosta.geekku.service;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.CommunityCommentDto;
import com.kosta.geekku.dto.CommunityDto;
import com.kosta.geekku.dto.CommunityFilterDto;
import com.kosta.geekku.entity.Community;
import com.kosta.geekku.entity.CommunityBookmark;
import com.kosta.geekku.entity.CommunityComment;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.CommunityBookmarkRepository;
import com.kosta.geekku.repository.CommunityCommentRepository;
import com.kosta.geekku.repository.CommunityRepository;
import com.kosta.geekku.repository.UserRepository;
import com.kosta.geekku.util.CommunitySpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommunityServiceImpl implements CommunityService {

	private final CommunityRepository communityRepository;
	private final CommunityBookmarkRepository communityBookmarkRepository;
	private final UserRepository userRepository;
	private final CommunityCommentRepository communityCommentRepository;

	@Value("${upload.path}")
	private String uploadPath;


	@Override
	public Page<CommunityDto> getCommunityList(Pageable pageable) {
	    return communityRepository.findAllByOrderByCreatedAtDesc(pageable)
	                              .map(community -> community.toDto());
	}


	@Override
	public Integer createCommunity(CommunityDto communityDto) {
		Community community = communityDto.toEntity();
		communityRepository.save(community);
		return community.getCommunityNum();
	}

	@Override
	public CommunityDto getCommunityDetail(Integer communityNum) {
		Community community =
		  communityRepository.findById(communityNum)
				.orElseThrow(() -> new IllegalArgumentException("커뮤니티 조회에 실패했습니다."));
		community.setViewCount(community.getViewCount()+1);
		communityRepository.save(community);
		return community.toDto();
	}


	@Override
	public Page<CommunityDto> getFilteredCommunityList(CommunityFilterDto filterDto, Pageable pageable) {
	    Specification<Community> specification = CommunitySpecification.filterBy(filterDto);
	    Pageable sortedPageable = PageRequest.of(
	        pageable.getPageNumber(),
	        pageable.getPageSize(),
	        Sort.by(Sort.Direction.DESC, "createdAt")
	    );
	    return communityRepository.findAll(specification, sortedPageable).map(Community::toDto);
	}


	@Transactional
	@Override
	public Integer createCommunityWithCoverImage(String title, String content, String type, MultipartFile coverImage,
			String userId, String address1, String address2, String familyType, String interiorType, Integer money,
			Date periodStartDate, Date periodEndDate, Integer size, String style) {
		UUID userUUID;
		try {
			userUUID = UUID.fromString(userId);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("유효하지 않은 userId 형식입니다.");
		}
		// userRepository에서 userId(UUID)로 사용자 조회
		User user = userRepository.findByUserId(userUUID); // UUID로 조회
		if (user == null) {
			throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
		}
		// 커뮤니티 객체 생성
		Community community = Community.builder().title(title) // 제목
				.content(content) // 내용
				.type(type) // 타입
				.user(user) // 사용자 (User 엔티티)
				.address1(address1) // 주소 1
				.address2(address2) // 주소 2
				.familyType(familyType) // 가족 유형
				.interiorType(interiorType) // 인테리어 유형
				.money(money) // 예산
				.periodStartDate(periodStartDate) // 시작 날짜
				.periodEndDate(periodEndDate) // 종료 날짜
				.size(size) // 평수
				.style(style) // 스타일
				.viewCount(0).build();
		community = communityRepository.save(community);
		//System.out.println("Community saved with ID: " + community.getCommunityNum());
		if (coverImage == null || coverImage.isEmpty()) {
			throw new IllegalArgumentException("파일이 비어 있거나 업로드되지 않았습니다.");
		}
		try {
			String communityUploadPath = uploadPath + "communityImage/";
			File uploadDir = new File(communityUploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}
			String fileName = coverImage.getOriginalFilename();
			if (fileName == null) {
				throw new IllegalArgumentException("파일 이름이 없습니다.");
			}
			String filePath = uploadPath + "communityImage/" + fileName;
			File dest = new File(filePath); // 파일 객체 생성
			coverImage.transferTo(dest);
			community.setCoverImage(fileName); // 파일 경로를 엔티티에 업데이트
			communityRepository.save(community); // 업데이트된 커뮤니티 저장
			//System.out.println("파일 경로가 데이터베이스에 저장되었습니다: " + fileName);
		} catch (IOException e) {
			e.printStackTrace();
			//System.err.println("파일 저장 중 오류 발생: " + e.getMessage());
			throw new RuntimeException("파일 저장에 실패했습니다. 경로를 확인하세요.");
		}
		return community.getCommunityNum();
	}

	@Override
	@Transactional
	public void updateCommunity(Integer id, CommunityDto communityDto, MultipartFile coverImage) throws Exception {
		// 기존 데이터 조회
		Community community = communityRepository.findById(id)
				.orElseThrow(() -> new Exception("해당 커뮤니티 글을 찾을 수 없습니다."));

		// 데이터 업데이트
		community.setTitle(communityDto.getTitle());
		community.setContent(communityDto.getContent());
		community.setType(communityDto.getType());
		community.setAddress1(communityDto.getAddress1());
		community.setAddress2(communityDto.getAddress2());
		community.setFamilyType(communityDto.getFamilyType());
		community.setInteriorType(communityDto.getInteriorType());
		community.setMoney(communityDto.getMoney());
		community.setPeriodStartDate(communityDto.getPeriodStartDate());
		community.setPeriodEndDate(communityDto.getPeriodEndDate());
		community.setSize(communityDto.getSize());
		community.setStyle(communityDto.getStyle());
		if (coverImage != null && !coverImage.isEmpty()) {
			// 기존 파일 삭제
			if (community.getCoverImage() != null) {
				File existingFile = new File(uploadPath, community.getCoverImage());
				if (existingFile.exists()) {
					existingFile.delete();
				}
			}
			try {
				File uploadDir = new File(uploadPath);
				if (!uploadDir.exists()) {
					uploadDir.mkdirs();
				}
				String fileName = coverImage.getOriginalFilename();
				String filePath = uploadPath + "/communityImage/" + fileName;
				coverImage.transferTo(new File(filePath));
				community.setCoverImage(fileName); // 파일 이름 업데이트
			} catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("파일 저장에 실패했습니다. 경로를 확인하세요.");
			}
		}

		// 변경 내용 저장
		communityRepository.save(community);
	}
	
	@Override
	@Transactional
	public Boolean getCommunityBookmark(String userId, Integer communityNum) throws Exception {
		// userId 값 검증
				if (userId == null || userId.isEmpty()) {
					//System.out.println("Invalid userId: " + userId);
					return false; // 사용자 ID가 없으면 false 반환
				}
				try {
					// UUID 변환 및 북마크 조회
					UUID userUuid = UUID.fromString(userId);
					CommunityBookmark existingBookmark = communityBookmarkRepository
							.findByUserUserIdAndCommunityCommunityNum(userUuid, communityNum);
					return existingBookmark != null; // 북마크가 존재하면 true 반환
				} catch (IllegalArgumentException e) {
					// userId가 UUID 형식이 아닌 경우 처리
					//System.out.println("Invalid UUID format: " + userId);
					return false;
				}
			}
	
	@Override
	@Transactional
	public boolean toggleCommunityBookmark(String userId, Integer communityNum) throws Exception {
		CommunityBookmark existingBookmark = communityBookmarkRepository
				.findByUserUserIdAndCommunityCommunityNum(UUID.fromString(userId), communityNum);
		if (existingBookmark == null) {
		//	User user = userRepository.findById(UUID.fromString(userId))
		//			.orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

			User user = userRepository.findByUserId(UUID.fromString(userId));
//					.orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다.")); <- 수정함

			Community community = communityRepository.findByCommunityNum(communityNum)
					.orElseThrow(() -> new IllegalArgumentException("해당 커뮤니티를 찾을 수 없습니다."));
			communityBookmarkRepository.save(CommunityBookmark.builder().user(user).community(community).build());
			return true; // 북마크 활성화
		} else {
			communityBookmarkRepository.delete(existingBookmark);
			return false; // 북마크 비활성화
		}
	}
	
	@Transactional
	@Override
	public List<CommunityCommentDto> getCommentsByCommunityId(Integer communityNum) {
		// communityNum에 해당하는 댓글들을 조회합니다.
		return communityCommentRepository.findByCommunityCommunityNum(communityNum).stream().map(cc -> cc.toDto())
				.collect(Collectors.toList());
	}


	@Transactional
	@Override
	public List<CommunityCommentDto> createComment(Integer communityId, String userId, String content) throws Exception {
		// 커뮤니티 게시글 확인
		Community community = communityRepository.findById(communityId)
				.orElseThrow(() -> new Exception("해당 커뮤니티 글을 찾을 수 없습니다."));
		// 사용자 확인
		User user = userRepository.findById(UUID.fromString(userId))
				.orElseThrow(() -> new Exception("해당 사용자를 찾을 수 없습니다."));
		// 댓글 엔티티 생성 및 저장
		CommunityComment comment = CommunityComment.builder().community(community).user(user).content(content).build();

		communityCommentRepository.save(comment);
		return communityCommentRepository.findByCommunityCommunityNum(communityId).stream()
	            .map(CommunityComment::toDto) // 엔티티를 DTO로 변환
	            .collect(Collectors.toList());
	}

	@Transactional
	@Override
	public void deleteComment(Integer commentNum) throws Exception {
		// 댓글 존재 여부 확인
		CommunityComment comment = communityCommentRepository.findById(commentNum)
				.orElseThrow(() -> new Exception("해당 댓글을 찾을 수 없습니다."));
		// 댓글 삭제
		communityCommentRepository.delete(comment);
	}

	@Override
	public User getUserProfile(String userId) throws Exception {
		return userRepository.findById(UUID.fromString(userId)).orElseThrow(() -> new Exception("해당 유저를 찾을 수 없습니다."));
	}

	@Override
	public List<CommunityDto> getUserCommunities(String userId) throws Exception {
	    List<Community> communities = communityRepository.findAllByUser_UserId(UUID.fromString(userId));

	    return communities.stream().map(Community::toDto) // Community 엔티티의 toDto 메서드를 사용
				.collect(Collectors.toList()); // 반환 타입은 List<CommunityDto>
	}


	@Override
	public List<CommunityDto> getCommunityListForMain() throws Exception {
		Page<Community> page = communityRepository.findAll(PageRequest.of(0, 3, Sort.by(Sort.Order.desc("viewCount"))));
        return page.getContent().stream().map(c -> c.toDto()).collect(Collectors.toList());
	}
	
	@Override
	public void increaseViewCount(Integer communityNum) {
		Community community = communityRepository.findById(communityNum)
				.orElseThrow(() -> new IllegalArgumentException("해당 커뮤니티 게시글이 없습니다."));
		community.setViewCount(community.getViewCount() + 1); // 조회수 증가
		communityRepository.save(community); // 변경된 값 저장
    }
	
	@Override
	@Transactional
	public void deleteCommunity(Integer communityNum) {
	    // 1. 댓글 데이터 삭제
	    communityCommentRepository.deleteByCommunity_CommunityNum(communityNum);

	    // 2. 커뮤니티 데이터 삭제
	    communityRepository.deleteById(communityNum);
	}

}
