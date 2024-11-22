package com.kosta.geekku;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.entity.Community;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.CommunityRepository;
import com.kosta.geekku.repository.UserRepository;

@SpringBootTest
class GeekkuApplicationTests {
	
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CommunityRepository communityRepository;

	@Test
	void contextLoads() {
		List<User> userList = userRepository.findAll();
		System.out.println(userList);
  }
	@Test
	void selectCompany() {
		List<Company> comList = companyRepository.findAll();
		System.out.println(comList);
  } 
  @Test
	void insertCommunity() {
		UUID userId = UUID.fromString("7e7506d5-b944-40c8-a269-c3c58d2067bb");
		Community community = Community.builder().title("test").content("Test")
				.user(User.builder().userId(userId).build()).build();
		communityRepository.save(community);
	}

//		Optional<User> ouser = userRepository.findById(UUID.fromString("bd05b491-df8d-4bea-a174-9186e9e9fb2d"));
//		if(ouser.isPresent()) {
//			System.out.println(ouser.get());
//		}
}
