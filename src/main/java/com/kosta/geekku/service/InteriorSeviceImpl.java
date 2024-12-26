package com.kosta.geekku.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
import com.sksamuel.scrimage.ImmutableImage;
import com.sksamuel.scrimage.webp.WebpWriter;

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
	private final FcmMessageService fcmMessageService;

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
	public List<InteriorDto> interiorList(String possibleLocation, PageInfo pageInfo, Integer limit) throws Exception {
		List<InteriorDto> interiorDtoList = null;
		Page<Interior> interiorPage = null;
		Long allCnt = 0L;
		Pageable pageable = PageRequest.of(pageInfo.getCurPage() - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
//		Integer offset = (pageInfo.getCurPage()-1) * limit;

		if (possibleLocation.equals("전체")) {
			interiorPage = interiorRepository.findAll(pageable);
//			interiorDtoList = interiorDslRepository.interiorListAll(pageable).stream().map(i -> i.toDto())
//					.collect(Collectors.toList());
//			allCnt = interiorDslRepository.interiorCountAll();
		} else {
			interiorPage = interiorRepository.findByPossibleLocationContains(possibleLocation, pageable);

//			interiorDtoList = interiorDslRepository.interiorListByLoc(possibleLocation, offset, limit).stream().map(i -> i.toDto())
//					.collect(Collectors.toList());
//			allCnt = interiorDslRepository.interiorCountByLoc(possibleLocation);
		}
		interiorDtoList = interiorPage.getContent().stream().map(i -> i.toDto()).collect(Collectors.toList());

		pageInfo.setTotalCount(1L*interiorPage.getTotalElements());

		pageInfo.setAllPage(interiorPage.getTotalPages());
		// pageInfo.setTotalCount(allCnt);
		return interiorDtoList;
	}

//
//	@Override
//	public List<SampleDto> sampleList(String date, String[] types, String[] styles, String[] sizes, String[] location,
//			PageInfo pageInfo, Integer limit) throws Exception {
//		List<SampleDto> sampleDtoList = null;
//		Page<InteriorSample> samplePage = null;
////		Page<InteriorSample> samplePage = null;
////		Long allCnt = 0L;
//		Pageable pageable = PageRequest.of(pageInfo.getCurPage() - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
//
//		if (types == null && styles == null && sizes == null && location == null) {
//			samplePage = interiorSampleRepository.findAll(pageable);
//		} else {
////			samplePage = interiorSampleRepository.findByTypeInOrStyleInOrSizeInOrLocationIn(Arrays.asList(type),
////					Arrays.asList(style), Arrays.asList(size), Arrays.asList(location), pageable);
//			
//			samplePage = interiorSampleRepository.findByTypeInOrStyleInOrSizeInOrLocationIn(types,styles,sizes,location, pageable);
//
//		}
//
//		sampleDtoList = samplePage.getContent().stream().map(s -> s.toDto()).collect(Collectors.toList());
//		System.out.println("----------------" + samplePage);
//		System.out.println("============" + sampleDtoList);
//		pageInfo.setAllPage(samplePage.getTotalPages());
//		pageInfo.setTotalCount(1L * samplePage.getTotalElements());
//
//		return sampleDtoList;
//
////		samplePage = interiorDslRepository.sampleListByFilter(date, type, style, size, location,pageable);
//
////		sampleDtoList = interiorDslRepository.sampleListByFilter(date, type, style, size, location).stream()
////				.map(s -> s.toDto()).collect(Collectors.toList());
////		allCnt = interiorDslRepository.sampleCountByFilter(date, type, style, size, location);
////		return sampleDtoList;
//	}
//	@Override
//	public List<SampleDto> sampleList(String date, String[] types, String[] styles, String[] sizes, String[] location,
//	                                  PageInfo pageInfo, Integer limit) throws Exception {
//	    Pageable pageable = PageRequest.of(pageInfo.getCurPage() - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
//	    List<InteriorSample> filteredSamples = new ArrayList<>();
//
//	    // 날짜 정렬 조건
//	    if ("latest".equals(date)) {
//	        pageable = PageRequest.of(pageInfo.getCurPage() - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
//	    } else if ("oldest".equals(date)) {
//	        pageable = PageRequest.of(pageInfo.getCurPage() - 1, limit, Sort.by(Sort.Direction.ASC, "createdAt"));
//	    }
//
//	    // 필터 조건이 없으면 전체 데이터 가져오기
//	    Page<InteriorSample> samplePage=null;
//	    if ((types == null || types.length == 0) && (styles == null || styles.length == 0)
//	            && (sizes == null || sizes.length == 0) && (location == null || location.length == 0)) {
//	        samplePage = interiorSampleRepository.findAll(pageable);  // 페이지 처리된 샘플 리스트
//	    } else {
//	        // 필터 조건에 맞는 샘플을 필터링
//	        Set<InteriorSample> sampleSet = new HashSet<>();
//
//	        // 타입에 맞는 샘플을 가져오기
//	        if (types != null && types.length > 0) {
//	            sampleSet.addAll(interiorSampleRepository.findByTypeIn(types, pageable).getContent());
//	        }
//
//	        // 스타일에 맞는 샘플을 가져오기
//	        if (styles != null && styles.length > 0) {
//	            sampleSet.addAll(interiorSampleRepository.findByStyleIn(styles, pageable).getContent());
//	        }
//
//	        // 사이즈에 맞는 샘플을 가져오기
//	        if (sizes != null && sizes.length > 0) {
//	            sampleSet.addAll(interiorSampleRepository.findBySizeIn(sizes, pageable).getContent());
//	        }
//
//	        // 위치에 맞는 샘플을 가져오기
//	        if (location != null && location.length > 0) {
//	            sampleSet.addAll(interiorSampleRepository.findByLocationIn(location, pageable).getContent());
//	        }
//
//	        // Set에서 List로 변환
//	        filteredSamples.addAll(sampleSet);
//	        // Page 객체를 사용하지 않으면, 페이지네이션을 제대로 처리할 수 없음
//	        samplePage = new PageImpl<>(filteredSamples, pageable, filteredSamples.size());
//	    }
//
//	    // Page 객체에서 필요한 정보 추출
//	    pageInfo.setAllPage(samplePage.getTotalPages());
//	    pageInfo.setTotalCount(samplePage.getTotalElements());
//
//	    // DTO로 변환하여 반환
//	    return samplePage.getContent().stream().map(s -> s.toDto()).collect(Collectors.toList());
//	}
//	@Override
//	public List<SampleDto> sampleList(String date, String[] types, String[] styles, String[] sizes, String[] location,
//	                                  PageInfo pageInfo, Integer limit) throws Exception {
//	    // Pageable 설정
//	    Pageable pageable = PageRequest.of(pageInfo.getCurPage() - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
//	    List<InteriorSample> filteredSamples = new ArrayList<>();
//	    Page<InteriorSample> samplePage=null;
//
//	    // 날짜 정렬 조건 설정
//	    if ("latest".equals(date)) {
//	        pageable = PageRequest.of(pageInfo.getCurPage() - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
//	    } else if ("oldest".equals(date)) {
//	        pageable = PageRequest.of(pageInfo.getCurPage() - 1, limit, Sort.by(Sort.Direction.ASC, "createdAt"));
//	    }
//
//	    // 필터 조건이 없으면 전체 데이터 가져오기
//	    if ((types == null || types.length == 0) && (styles == null || styles.length == 0)
//	            && (sizes == null || sizes.length == 0) && (location == null || location.length == 0)) {
//	        samplePage = interiorSampleRepository.findAll(pageable);  // 페이지 처리된 샘플 리스트
//	    } else {
//	        // 필터 조건에 맞는 샘플을 필터링
//	        List<InteriorSample> filteredByTypes = new ArrayList<>();
//	        List<InteriorSample> filteredByStyles = new ArrayList<>();
//	        List<InteriorSample> filteredBySizes = new ArrayList<>();
//	        List<InteriorSample> filteredByLocation = new ArrayList<>();
//
//	        // 조건에 맞는 샘플을 각각 가져오기
//	        if (types != null && types.length > 0) {
//	            filteredByTypes = interiorSampleRepository.findByTypeIn(types, pageable).getContent();
//	        }
//
//	        if (styles != null && styles.length > 0) {
//	            filteredByStyles = interiorSampleRepository.findByStyleIn(styles, pageable).getContent();
//	        }
//
//	        if (sizes != null && sizes.length > 0) {
//	            filteredBySizes = interiorSampleRepository.findBySizeIn(sizes, pageable).getContent();
//	        }
//
//	        if (location != null && location.length > 0) {
//	            filteredByLocation = interiorSampleRepository.findByLocationIn(location, pageable).getContent();
//	        }
//
//	        // 조건에 맞는 샘플들을 교집합으로 필터링
//	        if (!filteredByTypes.isEmpty()) {
//	            filteredSamples = new ArrayList<>(filteredByTypes);
//	        }
//
//	        if (!filteredByStyles.isEmpty()) {
//	            filteredSamples.retainAll(filteredByStyles); // 교집합 처리
//	        }
//
//	        if (!filteredBySizes.isEmpty()) {
//	            filteredSamples.retainAll(filteredBySizes); // 교집합 처리
//	        }
//
//	        if (!filteredByLocation.isEmpty()) {
//	            filteredSamples.retainAll(filteredByLocation); // 교집합 처리
//	        }
//
//	        // Page 객체를 사용하여 페이징 처리
//	        samplePage = new PageImpl<>(filteredSamples, pageable, filteredSamples.size());
//	    }
//
//	    // Page 객체에서 필요한 정보 추출
//	    pageInfo.setAllPage(samplePage.getTotalPages());
//	    pageInfo.setTotalCount(samplePage.getTotalElements());
//
//	    // DTO로 변환하여 반환
//	    return samplePage.getContent().stream().map(s -> s.toDto()).collect(Collectors.toList());
//	}
	@Override
	public List<SampleDto> sampleList(String date, String[] types, String[] styles, String[] sizes, String[] location,
			PageInfo pageInfo, Integer limit) throws Exception {
		// Pageable 설정
		Pageable pageable = PageRequest.of(pageInfo.getCurPage() - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
		List<InteriorSample> filteredSamples = new ArrayList<>();
		Page<InteriorSample> samplePage = null;

		// 날짜 정렬 조건 설정
		if ("latest".equals(date)) {
			pageable = PageRequest.of(pageInfo.getCurPage() - 1, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
		} else if ("oldest".equals(date)) {
			pageable = PageRequest.of(pageInfo.getCurPage() - 1, limit, Sort.by(Sort.Direction.ASC, "createdAt"));
		}

		// 필터 조건이 없으면 전체 데이터 가져오기
		if ((types == null || types.length == 0) && (styles == null || styles.length == 0)
				&& (sizes == null || sizes.length == 0) && (location == null || location.length == 0)) {
			samplePage = interiorSampleRepository.findAll(pageable); // 페이지 처리된 샘플 리스트
		} else {
			// 조건에 맞는 샘플들 필터링
			List<InteriorSample> filteredByTypes = null;
			List<InteriorSample> filteredByStyles = null;
			List<InteriorSample> filteredBySizes = null;
			List<InteriorSample> filteredByLocation = null;

			// 타입에 맞는 샘플 가져오기
			if (types != null && types.length > 0) {
				filteredByTypes = interiorSampleRepository.findByTypeIn(types, pageable).getContent();
			}

			// 스타일에 맞는 샘플 가져오기
			if (styles != null && styles.length > 0) {
				filteredByStyles = interiorSampleRepository.findByStyleIn(styles, pageable).getContent();
			}

			// 사이즈에 맞는 샘플 가져오기
//	        if (sizes != null && sizes.length > 0) {
//	        	
//	            filteredBySizes = interiorSampleRepository.findBySizeIn(sizes, pageable).getContent();
//	        }
			if (sizes != null && sizes.length > 0) {
				List<Integer> sizeRangeList = new ArrayList<>();

				for (String size : sizes) {
					if (size.contains("평대")) {
						// "20평대" -> 20~29
						Integer lowerBound = Integer.parseInt(size.substring(0, size.indexOf("평")));
						Integer upperBound = lowerBound + 9;
						for (int i = lowerBound; i <= upperBound; i++) {
							sizeRangeList.add(i);
						}
					} else if (size.contains("미만")) {
						// "10평미만" -> 1~9
						Integer upperBound = Integer.parseInt(size.substring(0, size.indexOf("평")));
						for (int i = 1; i < upperBound; i++) {
							sizeRangeList.add(i);
						}
					} else if (size.contains("이상")) {
						// "50평 이상" -> 50~상한값(100 예시)
						Integer lowerBound = Integer.parseInt(size.substring(0, size.indexOf("평")));
						for (int i = lowerBound; i <= 100; i++) { // 상한값을 적절히 설정
							sizeRangeList.add(i);
						}
					}
				}
				//System.out.println("============"+sizeRangeList);
				// List<Integer> -> Integer[]
				Integer[] sizeArray = sizeRangeList.toArray(new Integer[0]);

				// Repository 메서드 호출
				filteredBySizes = interiorSampleRepository.findBySizeIn(sizeArray, pageable).getContent();
			}

			// 위치에 맞는 샘플 가져오기
			if (location != null && location.length > 0) {
				filteredByLocation = interiorSampleRepository.findByLocationIn(location, pageable).getContent();
			}

			// 조건에 맞는 샘플들이 하나라도 선택되었을 경우
			if ((filteredByTypes != null && !filteredByTypes.isEmpty())
					|| (filteredByStyles != null && !filteredByStyles.isEmpty())
					|| (filteredBySizes != null && !filteredBySizes.isEmpty())
					|| (filteredByLocation != null && !filteredByLocation.isEmpty())) {

				// 각 조건에 맞는 샘플들 중에서 교집합을 구하기 위해 기본 샘플 리스트 설정
				filteredSamples.addAll(filteredByTypes != null ? filteredByTypes : new ArrayList<>());
				filteredSamples.addAll(filteredByStyles != null ? filteredByStyles : new ArrayList<>());
				filteredSamples.addAll(filteredBySizes != null ? filteredBySizes : new ArrayList<>());
				filteredSamples.addAll(filteredByLocation != null ? filteredByLocation : new ArrayList<>());

				// 교집합 구하기 위해 retainAll 사용
				if (filteredByTypes != null && !filteredByTypes.isEmpty()) {
					filteredSamples.retainAll(filteredByTypes); // 'types'에 해당하는 교집합만 남김
				}
				if (filteredByStyles != null && !filteredByStyles.isEmpty()) {
					filteredSamples.retainAll(filteredByStyles); // 'styles'에 해당하는 교집합만 남김
				}
				if (filteredBySizes != null && !filteredBySizes.isEmpty()) {
					filteredSamples.retainAll(filteredBySizes); // 'sizes'에 해당하는 교집합만 남김
				}
				if (filteredByLocation != null && !filteredByLocation.isEmpty()) {
					filteredSamples.retainAll(filteredByLocation); // 'location'에 해당하는 교집합만 남김
				}

				// 교집합이 비었으면 빈 리스트 반환
				if (filteredSamples.isEmpty()) {
					samplePage = new PageImpl<>(filteredSamples, pageable, 0); // 빈 리스트 반환
				} else {
					// 교집합이 있으면 페이지 처리
					samplePage = new PageImpl<>(filteredSamples, pageable, filteredSamples.size());
				}
			} else {
				// 조건이 하나도 선택되지 않으면 빈 리스트 반환
				filteredSamples = new ArrayList<>();
				samplePage = new PageImpl<>(filteredSamples, pageable, 0); // 빈 리스트 반환
			}
		}

		// 페이지 정보 설정
		pageInfo.setAllPage(samplePage.getTotalPages());
		pageInfo.setTotalCount(samplePage.getTotalElements());

		// DTO로 변환하여 반환
		return samplePage.getContent().stream().map(s -> s.toDto()).collect(Collectors.toList());
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
		Interior interior = interiorRepository.findById(interiorNum).orElseThrow(() -> new Exception("인테리어 번호 오류"));
		//System.out.println(interior);
//		Integer bookmarkNum = interiorDslRepository.findInteriorBookmark(UUID.fromString(userId), interiorNum);
//		System.out.println(bookmarkNum);
		if (interiorBookmark == null) {
			interiorBookmarkRepository
					.save(InteriorBookmark.builder().userId(UUID.fromString(userId)).interior(interior).build());
			return true;
		} else {
			interiorBookmarkRepository.deleteById(interiorBookmark.getBookmarkInteriorNum());
			return false;
		}
	}

	@Transactional
	@Override
	public Map<Object, Object> interiorRegister(InteriorDto interiorDto, MultipartFile coverImage, UUID companyId)
			throws Exception {
		Interior interior = interiorRepository.findByCompany_companyId(companyId);

		if (interior != null) {
			throw new Exception("이미 등록한 인테리어 회사입니다.");
		}

		Interior nInterior = interiorDto.toEntity();
		Company company = companyRepository.findById(companyId).orElseThrow(() -> new Exception("기업회원 찾기 오류"));
		company.setRegStatus(true);
		nInterior.setCompany(company);

		if (coverImage != null && !coverImage.isEmpty()) {
			nInterior.setCoverImage(coverImage.getBytes());
		}

		interiorRepository.save(nInterior);

		Map<Object, Object> total = new HashMap<>();
		total.put("regStatus", company.isRegStatus());
		total.put("interiorNum", nInterior.getInteriorNum());

		return total;
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
//			String fileName = coverImage.getOriginalFilename();
//			String filePath = uploadPath + "sampleImage/";
//
//			// 파일 저장 경로 확인 및 디렉토리 생성
//			File uploadDir = new File(filePath);
//			if (!uploadDir.exists()) {
//				uploadDir.mkdirs();
//			}
//
//			File file = new File(filePath + fileName);
//			coverImage.transferTo(file);
//			sample.setCoverImage(file.getName());
			  // 원본 파일 이름 및 경로
		    String originalFileName = coverImage.getOriginalFilename(); // e.g., "image.png"
		    String fileNameWithoutExt = originalFileName.substring(0, originalFileName.lastIndexOf(".")); // e.g., "image"
		    String filePath = uploadPath + "sampleImage/";

		    // 디렉토리 확인 및 생성
		    File uploadDir = new File(filePath);
		    if (!uploadDir.exists()) {
		        uploadDir.mkdirs();
		    }

		    // WebP 파일 경로 (원본 파일 이름과 동일, 확장자만 변경)
		    File webpFile = new File(filePath + fileNameWithoutExt + ".webp");

		    try {
		        // WebP 변환 및 저장
		        ImmutableImage.loader()
		                .fromStream(coverImage.getInputStream()) // 업로드된 파일의 입력 스트림
		                .output(WebpWriter.DEFAULT, webpFile); // WebP 형식으로 변환 후 저장

		        // DB에 저장할 파일명 설정
		        sample.setCoverImage(webpFile.getName());
		    } catch (Exception e) {
		        // 변환 실패 시 로그 출력 및 처리
		        System.err.println("Failed to convert image to WebP: " + e.getMessage());
		        throw e;
		    }
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
		Interior findInteriorNum = interiorRepository.findByCompany_companyId(findCompany);
		Integer num = findInteriorNum.getInteriorNum();

		review.setInterior(findInteriorNum);

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

				//System.out.println(reImage);
				interiorReviewImageRepository.save(reImage);

				File upFile = new File(uploadPath, reImage.getInteriorReviewImageNum() + "");
				try {
					file.transferTo(upFile);
				} catch (IOException e) {
					throw new Exception("파일 업로드 실패:" + e.getMessage(), e);
				}
			}
		}
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

		Interior interior = Interior.builder().interiorNum(request.getInterior().getInteriorNum()).build();
		request.setUser(user);
		request.setInterior(interior);
		interiorRequestRepository.save(request);
		Optional<Interior> oInterior = interiorRepository.findById(interior.getInteriorNum());
		// 알림 기능 추가
		requestDto.setCompanyId(oInterior.get().getCompany().getCompanyId());
		requestDto.setUserId(UUID.fromString(userId));
		//System.out.println(requestDto);
		fcmMessageService.sendInteriorRequest(requestDto);

		return request.getRequestNum();
	}

	@Override
	public InteriorRequestDto requestDetail(Integer num) throws Exception {
		InteriorRequest request = interiorRequestRepository.findById(num).orElseThrow(() -> new Exception("문의 번호 오류"));
		return request.toDto();
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
		review.setDate(reviewDto.getDate());

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
	public Map<String, Object> updateInteriorCompany(UUID companyId, InteriorDto interiorDto, MultipartFile coverImage)
			throws Exception {
		Interior interior = interiorRepository.findByCompany_companyId(companyId);
		try {
			byte[] imageBytes = interiorDto.getCoverImage(); // DTO에서 byte[] 가져오기

			if (imageBytes != null) {
				// byte[] 처리 (예: 데이터베이스에 저장)
				//System.out.println("Image size: " + imageBytes.length);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error processing image", e);
		}

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

		if (coverImage != null && !coverImage.isEmpty())

		{
			interior.setCoverImage(coverImage.getBytes());
		}

		interior.setPossiblePart(interiorDto.isPossiblePart());

		interiorRepository.save(interior);

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

	@Override
	public void sampleDelete(Integer sampleNum, UUID companyId) throws Exception {
		InteriorSample sample = interiorSampleRepository.findById(sampleNum).orElseThrow(() -> new Exception("시공사례 글번호 오류"));
		
		// upload_path에 저장된 coverImage 파일 삭제
		if (sample.getCoverImage() != null) {
			File delFile = new File(uploadPath + "sampleImage/" + sample.getCoverImage());
			delFile.delete();
		}
		
	    interiorSampleRepository.deleteById(sampleNum);
	}

	@Override
	public Integer sampleUpdate(SampleDto sampleDto, MultipartFile coverImage, MultipartFile deleteImage,
			UUID companyId, Integer num) throws Exception {
		
		InteriorSample sample = interiorSampleRepository.findById(num).orElseThrow(() -> new Exception("시공사례 글번호 오류"));
		sample.setContent(sampleDto.getContent());
		sample.setType(sampleDto.getType());
		sample.setTitle(sampleDto.getTitle());
		sample.setStyle(sampleDto.getStyle());
		sample.setSize(sampleDto.getSize());
		sample.setLocation(sampleDto.getLocation());
		interiorSampleRepository.save(sample);
		
		// 기존 coverImage 파일 삭제하는 경우
		if (deleteImage != null) {			
			File delFile = new File(uploadPath + "sampleImage/" + deleteImage.getOriginalFilename());
			if (delFile != null) delFile.delete();
		}
		
		// 수정된 coverImage파일 추가
		if (coverImage != null && !coverImage.isEmpty()) {
//			String fileName = coverImage.getOriginalFilename();
//			String filePath = uploadPath + "sampleImage/";
//
//			File file = new File(filePath + fileName);
//			coverImage.transferTo(file);
//			sample.setCoverImage(file.getName());
			
		    String originalFileName = coverImage.getOriginalFilename();
		    String fileNameWithoutExt = originalFileName.substring(0, originalFileName.lastIndexOf(".")); // e.g., "image"
		    String filePath = uploadPath + "sampleImage/";

		    // WebP 파일 경로 (원본 파일 이름과 동일, 확장자만 변경)
		    File webpFile = new File(filePath + fileNameWithoutExt + ".webp");

		    try {
		        // WebP 변환 및 저장
		        ImmutableImage.loader()
		                .fromStream(coverImage.getInputStream()) // 업로드된 파일의 입력 스트림
		                .output(WebpWriter.DEFAULT, webpFile); // WebP 형식으로 변환 후 저장

		        sample.setCoverImage(webpFile.getName());
		    } catch (Exception e) {
		        // 변환 실패 시 로그 출력 및 처리
		        System.err.println("Failed to convert image to WebP: " + e.getMessage());
		        throw e;
		    }
		}
		
		interiorSampleRepository.save(sample);
		
		return sample.getSampleNum();
	}
}