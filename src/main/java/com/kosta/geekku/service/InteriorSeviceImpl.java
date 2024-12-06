package com.kosta.geekku.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosta.geekku.config.jwt.JwtToken;
import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.dto.InteriorRequestDto;
import com.kosta.geekku.dto.ReviewDto;
import com.kosta.geekku.dto.SampleDto;
import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.Interior;
import com.kosta.geekku.entity.InteriorBookmark;
import com.kosta.geekku.entity.InteriorRequest;
import com.kosta.geekku.entity.InteriorReview;
import com.kosta.geekku.entity.InteriorReviewImage;
import com.kosta.geekku.entity.InteriorSample;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.repository.InteriorBookmarkRepository;
import com.kosta.geekku.repository.InteriorDslRepository;
import com.kosta.geekku.repository.InteriorRepository;
import com.kosta.geekku.repository.InteriorRequestDslRepository;
import com.kosta.geekku.repository.InteriorRequestRepository;
import com.kosta.geekku.repository.InteriorReviewImageRepository;
import com.kosta.geekku.repository.InteriorReviewRepository;
import com.kosta.geekku.repository.InteriorSampleDslRepository;
import com.kosta.geekku.repository.InteriorSampleRepository;
import com.kosta.geekku.repository.UserRepository;
import com.kosta.geekku.util.PageInfo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InteriorSeviceImpl implements InteriorService {

	private final InteriorRepository interiorRepository;
	private final InteriorDslRepository interiorDslRepository;
	private final InteriorBookmarkRepository interiorBookmarkRepository;
	private final UserRepository userRepository;
	private final InteriorSampleRepository interiorSampleRepository;
	private final InteriorSampleDslRepository interiorSampleDslRepository;
	private final InteriorReviewRepository interiorReviewRepository;
	private final InteriorRequestRepository interiorRequestRepository;
	private final InteriorRequestDslRepository interiorRequestDslRepository;
	private final InteriorReviewImageRepository interiorReviewImageRepository;
	private final CompanyRepository companyRepository;

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private JwtToken jwtToken;

	@Value("${upload.path}")
	private String uploadPath;

	@Override
	public List<InteriorDto> interiorListForMain() throws Exception {
		List<InteriorDto> interiorDtoList = null;
		interiorDtoList = interiorDslRepository.findInteriorListForMain().stream().map(i -> i.toDto())
				.collect(Collectors.toList());
		return interiorDtoList;
	}

	@Override
	public List<SampleDto> sampleListForMain() throws Exception {
		List<SampleDto> sampleList = null;
		sampleList = interiorDslRepository.findSampleListForMain().stream().map(s -> s.toDto())
				.collect(Collectors.toList());
		return sampleList;
	}

	@Override
	public List<InteriorDto> interiorList(String possibleLocation) throws Exception {
		List<InteriorDto> interiorDtoList = null;
		Long allCnt = 0L;
		if (possibleLocation.equals("전체")) {
			interiorDtoList = interiorDslRepository.interiorListAll().stream().map(i -> i.toDto())
					.collect(Collectors.toList());
			allCnt = interiorDslRepository.interiorCountAll();
		} else {
			interiorDtoList = interiorDslRepository.interiorListByLoc(possibleLocation).stream().map(i -> i.toDto())
					.collect(Collectors.toList());
			allCnt = interiorDslRepository.interiorCountByLoc(possibleLocation);
		}
		return interiorDtoList;
	}

	@Override
	public Integer checkBookmark(String userId, Integer interiorNum) throws Exception {
		return interiorDslRepository.findInteriorBookmark(UUID.fromString(userId), interiorNum);
	}

	@Override
	@Transactional
	public boolean toggleBookmark(String userId, Integer interiorNum) throws Exception {
		InteriorBookmark interiorBookmark = interiorBookmarkRepository.findByInterior_InteriorNumAndUserId(interiorNum,
				UUID.fromString(userId));
		System.out.println(interiorBookmark);
		Interior interior = interiorRepository.findById(interiorNum).orElseThrow(() -> new Exception("인테리어 번호 오류"));
		System.out.println(interior);
		Integer bookmarkNum = interiorDslRepository.findInteriorBookmark(UUID.fromString(userId), interiorNum);
		System.out.println(bookmarkNum);
		if (bookmarkNum == null) {
			interiorBookmarkRepository
					.save(InteriorBookmark.builder().userId(UUID.fromString(userId)).interior(interior).build());
			return true;
		} else {
			interiorBookmarkRepository.deleteById(bookmarkNum);
			return false;
		}
	}

	@Transactional
	@Override
	public Integer interiorRegister(InteriorDto interiorDto, MultipartFile coverImage, UUID companyId)
			throws Exception {
		Interior interior = interiorRepository.findByCompany_companyId(companyId);

		if (interior != null) {
			throw new Exception("이미 등록한 인테리어 회사입니다.");
		}

		Interior nInterior = interiorDto.toEntity();
		Company company = companyRepository.findById(companyId).orElseThrow(() -> new Exception("기업회원 찾기 오류"));
		nInterior.setCompany(company);

		if (coverImage != null && !coverImage.isEmpty()) {
			nInterior.setCoverImage(coverImage.getBytes());
		}

		interiorRepository.save(nInterior);

		return nInterior.getInteriorNum();
	}

	@Override
	public InteriorDto interiorCompanyDetail(UUID companyId) throws Exception {
		Interior interior = interiorRepository.findByCompany_companyId(companyId);

		return interior.toDto();
	}

	@Transactional
	@Override
	public Integer sampleRegister(SampleDto sampleDto, MultipartFile coverImage, UUID companyId) throws Exception {
		InteriorSample sample = sampleDto.toEntity();
		Company company = companyRepository.findById(companyId).orElseThrow(() -> new Exception("기업회원 찾기 오류"));
		Interior interior = interiorRepository.findByCompany_companyId(companyId);
		sample.setCompany(company);
		sample.setInterior(interior);

		if (coverImage != null && !coverImage.isEmpty()) {
			String fileName = coverImage.getOriginalFilename();
			String filePath = uploadPath + "sampleImage/";

			// 파일 저장 경로 확인 및 디렉토리 생성
			File uploadDir = new File(filePath);
			if (!uploadDir.exists()) {
				uploadDir.mkdirs();
			}

			File file = new File(filePath + fileName);
			coverImage.transferTo(file);
			sample.setCoverImage(file.getName());
		}

		interiorSampleRepository.save(sample);

		return sample.getSampleNum();
	}

	@Transactional
	@Override
	public Integer reviewRegister(String userId, ReviewDto reviewDto, List<MultipartFile> fileList) throws Exception {

		InteriorReview review = reviewDto.toEntity();

		Company company = companyRepository.findByCompanyNameContaining(reviewDto.getCompanyName()); // 리뷰 등록할 때 회사이름
																										// 일부만 작성했을수도
																										// 있기때문에 포함된 회사
																										// 조회

		UUID findCompany = company.getCompanyId();

		System.out.println(findCompany);

		Interior findInteriorNum = interiorRepository.findByCompany_companyId(findCompany);

		Integer num = findInteriorNum.getInteriorNum();

		System.out.println(num);

		review.setInterior(findInteriorNum);

//		Interior interior = interiorRepository.findByCompany_companyNameContaining(findCompany);
//		
//		Integer findInteriorNum = interior.getInteriorNum();

//		System.out.println(findCompany);

//		Integer collectComNum = interior.getInteriorNum();

		User user = User.builder().userId(UUID.fromString(userId)).build();

		review.setUser(user);
		interiorReviewRepository.save(review);

		if (fileList != null && fileList.size() > 0) {
			for (MultipartFile file : fileList) {
				InteriorReviewImage reImage = new InteriorReviewImage();
				reImage.setDierctory(uploadPath);
				reImage.setName(file.getOriginalFilename());
				reImage.setContentType(file.getContentType());
				reImage.setSize(file.getSize());
				reImage.setInteriorReview(review);

				System.out.println(reImage);
				interiorReviewImageRepository.save(reImage);

				File upFile = new File(uploadPath, reImage.getInteriorReviewImageNum() + "");
				try {
					file.transferTo(upFile);
				} catch (IOException e) {
					throw new Exception("파일 업로드 실패:" + e.getMessage(), e);
				}
			}
		}
		System.out.println(review.getReviewNum());
		return review.getReviewNum();
	}

	@Override
	public SampleDto sampleDetail(Integer num) throws Exception {
		InteriorSample sample = interiorSampleRepository.findById(num).orElseThrow(() -> new Exception("사례 번호 오류"));
		return sample.toDto();
	}

	@Transactional
	@Override
	public Integer interiorRequest(String userId, InteriorRequestDto requestDto) throws Exception {
		InteriorRequest request = requestDto.toEntity();
		User user = User.builder().userId(UUID.fromString(userId)).build();
		Interior interior = Interior.builder().interiorNum(1).build(); // test용 interiorNum 1 대입

		request.setUser(user);
		request.setInterior(interior);
		interiorRequestRepository.save(request);
		return request.getRequestNum();
	}

	@Override
	public InteriorRequestDto requestDetail(Integer num) throws Exception {
		InteriorRequest request = interiorRequestRepository.findById(num).orElseThrow(() -> new Exception("문의 번호 오류"));
		return request.toDto();
	}

	@Override
	public List<SampleDto> sampleList(String date, String[] type, String[] style, String[] size, String[] location)
			throws Exception {
		List<SampleDto> sampleDtoList = null;
		Long allCnt = 0L;
		sampleDtoList = interiorDslRepository.sampleListByFilter(date, type, style, size, location).stream()
				.map(s -> s.toDto()).collect(Collectors.toList());
		allCnt = interiorDslRepository.sampleCountByFilter(date, type, style, size, location);
		return sampleDtoList;
	}

	@Override
	public Map<String, Object> interiorDetail(Integer interiorNum) throws Exception {
		Map<String, Object> detailInfo = new HashMap<>();
		Interior interiorDetail = interiorRepository.findById(interiorNum)
				.orElseThrow(() -> new Exception("인테리어 번호 오류"));
		List<InteriorSample> sampleDetail = interiorSampleRepository.findByInterior_InteriorNum(interiorNum);
		List<InteriorReview> reviewDetail = interiorReviewRepository.findByInterior_interiorNum(interiorNum);

		Integer sampleCount = sampleDetail.size();
		Integer reviewCount = reviewDetail.size();

		InteriorDto interiorInfo = interiorDetail.toDto();
		List<SampleDto> sampleInfo = sampleDetail.stream().map(s -> s.toDto()).collect(Collectors.toList());
		List<ReviewDto> reviewInfo = reviewDetail.stream().map(r -> r.toDto()).collect(Collectors.toList());

		detailInfo.put("sampleCount", sampleCount);
		detailInfo.put("reviewCount", reviewCount);
		detailInfo.put("interiorDetail", interiorInfo);
		detailInfo.put("sampleDetail", sampleInfo);
		detailInfo.put("reviewDetail", reviewInfo);

		return detailInfo;
	}

	public Page<InteriorRequestDto> interiorRequestListForUserMypage(int page, int size, UUID userId) throws Exception {
		Optional<User> user = userRepository.findById(userId);

		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<InteriorRequestDto> pageInfo = interiorRequestRepository.findAllByUser(user, pageable)
				.map(InteriorRequest::toDto);

		return pageInfo;
	}

	@Override
	public Page<ReviewDto> reviewListForUserMypage(int page, int size, UUID userId) throws Exception {
		Optional<User> user = userRepository.findById(userId);

		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<ReviewDto> pageInfo = interiorReviewRepository.findAllByUser(user, pageable).map(InteriorReview::toDto);

		return pageInfo;
	}

	@Override
	public Integer updateReview(ReviewDto reviewDto, Integer num, List<Integer> delFileNum,
			List<MultipartFile> fileList) throws Exception {
		InteriorReview review = interiorReviewRepository.findById(num)
				.orElseThrow(() -> new Exception("인테리어 후기 글번호 오류"));

		review.setContent(reviewDto.getContent());
		review.setSize(reviewDto.getSize());
		review.setLocation(reviewDto.getLocation());
		review.setStyle(reviewDto.getStyle());
		review.setType(reviewDto.getType());

		interiorReviewRepository.save(review);

		// 기존 이미지파일 삭제하는 경우
		if (delFileNum != null) {
			for (Integer fn : delFileNum) {
				File oldFile = new File(uploadPath, fn + "");
				if (oldFile != null)
					oldFile.delete();
				interiorReviewImageRepository.deleteById(fn);
			}
		}

		// 이미지파일 추가
		if (fileList != null && fileList.size() > 0) {
			for (MultipartFile file : fileList) {
				InteriorReviewImage bFile = new InteriorReviewImage();
				bFile.setDierctory(uploadPath);
				bFile.setName(file.getOriginalFilename());
				bFile.setSize(file.getSize());
				bFile.setContentType(file.getContentType());
				bFile.setInteriorReview(review);
				interiorReviewImageRepository.save(bFile);

				File nFile = new File(uploadPath, bFile.getInteriorReviewImageNum() + "");
				file.transferTo(nFile);
			}
		}

		return review.getReviewNum();
	}

	@Override
	public void deleteReview(Integer num) throws Exception {
		InteriorReview review = interiorReviewRepository.findById(num)
				.orElseThrow(() -> new Exception("인테리어 후기 글번호 오류"));
		interiorReviewRepository.deleteById(num);
	}

	/*
	 * return interiorRequestDtoList;
	 * 
	 * public Page<InteriorRequestDto> interiorRequestListForUserMypage(int page,
	 * int size, UUID userId) throws Exception { Optional<User> user =
	 * userRepository.findById(userId);
	 * 
	 * Pageable pageable = PageRequest.of(page - 1, size,
	 * Sort.by(Sort.Direction.DESC, "createdAt")); Page<InteriorRequestDto> pageInfo
	 * = interiorRequestRepository.findAllByUser(user, pageable)
	 * .map(InteriorRequest::toDto);
	 * 
	 * return pageInfo; }
	 */

	@Override
	public List<SampleDto> interiorSampleList(PageInfo pageInfo, UUID companyId) throws Exception {
		PageRequest pageRequest = PageRequest.of(pageInfo.getCurPage() - 1, 10);
		List<SampleDto> interiorSampleDtoList = interiorDslRepository.interiorSampleListmypage(pageRequest, (companyId))
				.stream().map(e -> e.toDto()).collect(Collectors.toList());
		Long allCnt = interiorDslRepository.findMypageEstateCount(companyId);

		Integer allPage = (int) (Math.ceil(allCnt.doubleValue() / pageRequest.getPageSize()));
		Integer startPage = (pageInfo.getCurPage() - 1) / 10 * 10;
		Integer endPage = Math.min(startPage + 10 - 1, allPage);

		pageInfo.setAllPage(allPage);
		pageInfo.setStartPage(startPage);
		pageInfo.setEndPage(endPage);

		return interiorSampleDtoList;
	}

	@Override
	public void deleteRequest(Integer requestNum) throws Exception {
		InteriorRequest InteriorRequest = interiorRequestRepository.findById(requestNum)
				.orElseThrow(() -> new Exception("인테리어 문의 글번호 오류"));
		interiorRequestRepository.deleteById(requestNum);
	}

	@Override
	public Map<String, Object> updateInteriorCompany(UUID companyId, InteriorDto interiorDto, MultipartFile file) {
		Interior interior = interiorRepository.findByCompany_companyId(companyId);

		// User user = userRepository.findById(userId).orElseThrow(() -> new
		// Exception("사용자를 찾을 수 없습니다"));
		// Interior interior = interiorDto.toEntity();

		if (interiorDto.getIntro() != null)
			interior.setIntro(interiorDto.getIntro());

		if (interiorDto.getContent() != null)
			interior.setContent(interiorDto.getContent());
		if (interiorDto.getPossibleLocation() != null)
			interior.setPossibleLocation(interiorDto.getPossibleLocation());

		if (interiorDto.getPeriod() != null)
			interior.setPeriod(interiorDto.getPeriod());

		if (interiorDto.getRecentCount() != null)
			interior.setRecentCount(interiorDto.getRecentCount());

		if (interiorDto.getRepairDate() != null)
			interior.setRepairDate(interiorDto.getRepairDate());

		if (file != null && !file.isEmpty()) {
			interior.setCoverImage(interiorDto.getCoverImage());
		}

		interiorRepository.save(interior);

		System.out.println("int" + interior);

		Map<String, Object> res = new HashMap<>();
		res.put("interior", interior.toDto());
		return res;
	}

	public ReviewDto getReview(Integer reviewNum) throws Exception {
		InteriorReview review = interiorReviewRepository.findById(reviewNum)
				.orElseThrow(() -> new Exception("인테리어 후기 글번호 오류"));
		return review.toDto();

	}

	@Override
	public Page<InteriorRequestDto> interiorRequestList(int page, int size, UUID companyId) throws Exception {
		Optional<Interior> interiorNum = interiorRepository.findNumByCompany_companyId(companyId);

		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<InteriorRequestDto> pageInfo = interiorRequestRepository.findAllByInterior(interiorNum, pageable)
				.map(InteriorRequest::toDto);
		return pageInfo;
	}

	@Override
	public Page<ReviewDto> interiorReviewList(int page, int size, UUID companyId) throws Exception {
		Optional<Interior> interiorNum = interiorRepository.findNumByCompany_companyId(companyId);

		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		Page<ReviewDto> pageInfo = interiorReviewRepository.findAllByInterior(interiorNum, pageable)
				.map(InteriorReview::toDto);

		return pageInfo;

	}

//	@Override
//	public Page<ReviewDto> reviewListForUserMypage(int page, int size, UUID userId) throws Exception {
//		Optional<User> user = userRepository.findById(userId);
//
//		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//		Page<ReviewDto> pageInfo = interiorReviewRepository.findAllByUser(user, pageable).map(InteriorReview::toDto);
//
//		return pageInfo;
//	}

}