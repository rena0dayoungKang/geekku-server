package com.kosta.geekku.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kosta.geekku.config.jwt.JwtProperties;
import com.kosta.geekku.config.jwt.JwtToken;
import com.kosta.geekku.dto.CompanyDto;
import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.Estate;
import com.kosta.geekku.entity.HouseAnswer;
import com.kosta.geekku.entity.OnestopAnswer;
import com.kosta.geekku.entity.UFile;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.repository.EstateRepository;
import com.kosta.geekku.repository.HouseAnswerRepository;
import com.kosta.geekku.repository.OnestopAnswerRepository;
import com.kosta.geekku.repository.UFileRepository;

@Service
public class CompanyServiceImpl implements CompanyService {

	@Autowired
	private JwtToken jwtToken;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private EstateRepository estateRepository;
	@Autowired
	private HouseAnswerRepository houseAnswerRepository;
	@Autowired
	private OnestopAnswerRepository onestopAnswerRepository;
	@Autowired
	private UFileRepository uFileRepository;
	@Autowired
	private ObjectMapper objectMapper;

	@Value("${upload.path}")
	private String uploadPath;

	@Override
	public void joinCompany(CompanyDto companyDto, MultipartFile file) throws Exception {
		// type이 "부동산" 인 경우 estateNumber가 null이면 예외처리
		if ("estate".equals(companyDto.getType()) && isNullOrEmpty(companyDto.getEstateNumber())) {
			throw new IllegalArgumentException("부동산타입은 중개등록번호 입력해야함");
		}

		Company company = companyDto.toEntity();

		// 회원가입시 기본이미지 설정
		byte[] defaultProfileImage = Files.readAllBytes(Paths.get("src/main/resources/static/img/profileImg.png"));
		company.setProfileImage(defaultProfileImage);

		companyRepository.save(company);

		// 사업자 등록증 이미지 첨부
		Integer certificationImageNum = null;
		if (file != null && !file.isEmpty()) {
			String companyUploadPath = uploadPath + "/company/";
			File uploadDir = new File(companyUploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}

			String fileName = companyDto.getUsername() + "_" + file.getOriginalFilename();
			File nFile = new File(companyUploadPath, fileName);
			file.transferTo(nFile);

			UFile uFile = new UFile();
			uFile.setDirectory(uploadPath);
			uFile.setName(file.getOriginalFilename());
			uFile.setContentType(file.getContentType());
			uFile.setSize(file.getSize());
			uFile.setCompany(company);
			uFile.setUsername(companyDto.getUsername());
			uFileRepository.save(uFile);

			certificationImageNum = uFile.getUserImageNum();
		}
	}

	private boolean isNullOrEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

	@Override
	public CompanyDto login(String username, String password) throws Exception {
		Company company = companyRepository.findByUsername(username).orElseThrow(() -> new Exception("아이디 오류"));
		if (!company.getPassword().equals(password)) {
			throw new Exception("비밀번호 오류");
		}
		return company.toDto();
	}

	@Override
	public CompanyDto getCompany(String username) throws Exception {
		Company company = companyRepository.findByUsername(username).orElseThrow(() -> new Exception("사용자 아이디 오류"));

		Optional<UFile> uFileOpt = uFileRepository.findByCompany(company);
		String certificationImagePath = null;
		if (uFileOpt.isPresent()) {
			UFile uFile = uFileOpt.get();
			certificationImagePath = "/resources/company/" + company.getUsername() + "_" + uFile.getName();
		}

		CompanyDto companyDto = company.toDto();
		companyDto.setCertificationImagePath(certificationImagePath);

		return companyDto;
	}

	@Override
	public CompanyDto getCompany(UUID companyId) throws Exception {
		Company company = companyRepository.findById(companyId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
		return company.toDto();
	}

	@Override
	public Map<String, Object> updateCompanyInfo(UUID companyId, CompanyDto companyDto, MultipartFile profile,
			MultipartFile cerftFile) throws Exception {
		Company company = companyRepository.findById(companyId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다"));
		if (companyDto.getCompanyAddress() != null)
			company.setCompanyAddress(companyDto.getCompanyAddress());
		if (companyDto.getPhone() != null)
			company.setPhone(companyDto.getPhone());
		if (companyDto.getEmail() != null)
			company.setEmail(companyDto.getEmail());

		if (profile != null && !profile.isEmpty()) {
			company.setProfileImage(profile.getBytes());
		}

		if (cerftFile != null && !cerftFile.isEmpty()) {
			String companyUploadPath = uploadPath + "/company/";

			UFile oFile = uFileRepository.findByCompany(company).orElse(null);
			if (oFile != null) {
				File bfile = new File(companyUploadPath, company.getUsername() + "_" + oFile.getName());
				if (bfile.exists()) {
					bfile.delete();
				}
				uFileRepository.delete(oFile);
			}

			String newFileName = company.getUsername() + "_" + cerftFile.getOriginalFilename();
			File newFile = new File(companyUploadPath, newFileName);

			File uploadDir = new File(companyUploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}

			cerftFile.transferTo(newFile);

			UFile newCertFile = new UFile();
			newCertFile.setDirectory(uploadPath);
			newCertFile.setName(cerftFile.getOriginalFilename());
			newCertFile.setContentType(cerftFile.getContentType());
			newCertFile.setSize(cerftFile.getSize());
			newCertFile.setCompany(company);
			newCertFile.setUsername(company.getUsername());
			uFileRepository.save(newCertFile);
		}

		companyRepository.save(company);

		CompanyDto resultDto = company.toDto();
		UFile finalFile = uFileRepository.findByCompany(company).orElse(null);
		if (finalFile != null) {
			resultDto.setCertificationImagePath(
					"/resources/company/" + company.getUsername() + "_" + finalFile.getName());
		}

		String newAccessToken = jwtToken.makeAccessToken(company.getUsername(), company.getRole().toString());
		String newRefreshToken = jwtToken.makeRefreshToken(company.getUsername(), company.getRole().toString());

		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", JwtProperties.TOKEN_PREFIX + newAccessToken);
		tokens.put("refresh_token", JwtProperties.TOKEN_PREFIX + newRefreshToken);

		Map<String, Object> res = new HashMap<>();
		res.put("token", objectMapper.writeValueAsString(tokens));
		res.put("company", resultDto);

		// System.out.println("result Dto : " + resultDto);

		return res;
	}

	@Override
	public Map<String, Object> changePassword(UUID companyId, String newPassword) throws Exception {
		Company company = companyRepository.findById(companyId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다"));
		company.setPassword(newPassword);
		companyRepository.save(company);

		String newAccessToken = jwtToken.makeAccessToken(company.getUsername(), company.getRole().toString());
		String newRefreshToken = jwtToken.makeRefreshToken(company.getUsername(), company.getRole().toString());
		Map<String, String> tokens = new HashMap<>();
		tokens.put("access_token", JwtProperties.TOKEN_PREFIX + newAccessToken);
		tokens.put("refresh_token", JwtProperties.TOKEN_PREFIX + newRefreshToken);

		Map<String, Object> res = new HashMap<>();
		res.put("token", objectMapper.writeValueAsString(tokens));
		res.put("company", company.toDto());

		return res;
	}

	@Override
	public CompanyDto getCompanyProfile(String companyId) {

		Company company = companyRepository.findById(UUID.fromString(companyId)).orElseThrow();
		return CompanyDto.builder().companyName(company.getCompanyName()).email(company.getEmail()) // email로 수정
				.username(company.getUsername()).build();

	}

	@Override //
	public List<Estate> getEstateCommunities(String companyId) throws Exception {
		return estateRepository.findByCompany_CompanyId(UUID.fromString(companyId));
	}

	@Override
	public void deleteEstateCommunity(Integer estateId) throws Exception {
		Estate estate = estateRepository.findById(estateId).orElseThrow(() -> new Exception("해당 게시글을 찾을 수 없습니다."));
		estateRepository.delete(estate);
	}

	@Override
	public Page<HouseAnswer> getAnswersByCompanyId(UUID companyId, Pageable pageable) {
		return houseAnswerRepository.findByCompanyId(companyId, pageable);
	}

	@Override
	public Page<OnestopAnswer> getOnestopAnswersByCompanyId(UUID companyId, Pageable pageable) {
		return onestopAnswerRepository.findByCompanyId(companyId, pageable);
	}

	@Override
	public String getCompanyCertificationImagePath(Integer num) throws Exception {
		UFile uFile = uFileRepository.findById(num).orElseThrow(() -> new Exception("파일을 찾을 수 없습니다."));
		String filePath = uFile.getDirectory() + "/company/" + uFile.getName();
		File file = new File(filePath);
		if (!file.exists()) {
			throw new Exception("파일이 존재하지 않습니다.");
		}
		return filePath;
	}

	@Override
	public List<CompanyDto> findIdByEmail(String email) throws Exception {
		List<CompanyDto> companyList = companyRepository.findAllByEmail(email).stream().map(e -> e.toDto())
				.collect(Collectors.toList());
		return companyList;
	}
}
