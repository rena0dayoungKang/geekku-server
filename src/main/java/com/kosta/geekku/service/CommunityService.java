package com.kosta.geekku.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.CommunityCommentDto;
import com.kosta.geekku.dto.CommunityDto;
import com.kosta.geekku.dto.CommunityFilterDto;
import com.kosta.geekku.entity.User;

public interface CommunityService {

	Page<CommunityDto> getCommunityList(Pageable pageable);

	Integer createCommunity(CommunityDto communityDto);

	CommunityDto getCommunityDetail(Integer communityNum);

	Page<CommunityDto> getFilteredCommunityList(CommunityFilterDto filterDto, Pageable pageable);

	Integer createCommunityWithCoverImage(String title, String content, String type, MultipartFile coverImage,
			String userId, String address1, String address2, String familyType, String interiorType, Integer money,
			Date periodStartDate, Date periodEndDate, Integer size, String style);

	void updateCommunity(Integer id, CommunityDto communityDto, MultipartFile coverImage) throws Exception;

	boolean toggleCommunityBookmark(String userId, Integer communityNum) throws Exception;

	List<CommunityCommentDto> getCommentsByCommunityId(Integer communityNum) throws Exception;

	void createComment(Integer communityId, String userId, String content) throws Exception;

	void deleteComment(Integer commentId) throws Exception;

	User getUserProfile(String user) throws Exception;

	List<CommunityDto> getUserCommunities(String userId) throws Exception;

	List<CommunityDto> getCommunityListForMain() throws Exception;

	void increaseViewCount(Integer communityNum) throws Exception;

	void deleteCommunity(Integer communityNum) throws Exception;

	Boolean getCommunityBookmark(String userId, Integer communityNum) throws Exception;

}
