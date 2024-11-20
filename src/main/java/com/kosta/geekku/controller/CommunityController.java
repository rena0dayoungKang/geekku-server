package com.kosta.geekku.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.kosta.geekku.dto.CommunityDto;
import com.kosta.geekku.repository.CommunityRepository;
import com.kosta.geekku.service.CommunityService;

@RestController
public class CommunityController {

	@Autowired
    private CommunityRepository communityRepository;
	@Autowired
	private CommunityService communityService;

    // 모든 커뮤니티 게시글 반환
    @GetMapping("/communityList")
    public List<CommunityDto> getAllCommunities() {
        return communityRepository.findAll().stream().map(c->c.toDto()).collect(Collectors.toList());
    }

    // 게시글 생성
    @PostMapping("/communityCreate")
    public ResponseEntity<String> createCommunity(
            CommunityDto communityDto,
            @RequestParam(name = "coverImage", required = false) MultipartFile[] files) {
        try {
            Integer communityNum = communityService.createCommunity(communityDto, files == null ? null : List.of(files));
            return ResponseEntity.ok(String.valueOf(communityNum));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("커뮤니티 글 작성 실패");  
        }
    }

    @GetMapping("/communityBoardDetail/{community_num}")
    public ResponseEntity<CommunityDto> getCommunityDetail(@PathVariable Integer community_num) {
        try {
            CommunityDto communityDto = communityService.getCommunityDetail(community_num);
            return new ResponseEntity<>(communityDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


}