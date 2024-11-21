package com.kosta.geekku;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
//import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.repository.UserRepository;

@SpringBootTest
class GeekkuApplicationTests {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CompanyRepository companyRepository;


	@Test
	void contextLoads() {
//		List<User> userList = userRepository.findAll();
//		System.out.println(userList);
		
		Optional<User> ouser = userRepository.findById(UUID.fromString("f0e76f95-4894-499b-9da3-6ee2c6cecffe"));
		if(ouser.isPresent()) {
			System.out.println(ouser.get());
		}
	}
	@Test
	void selectCompany() {
		List<Company> comList = companyRepository.findAll();
		System.out.println(comList);
		
//		Optional<User> ouser = userRepository.findById(UUID.fromString("45dc40fa-ce43-458d-8f8f-6865f8a72ecb"));
//		if(ouser.isPresent()) {
//			System.out.println(ouser.get());
//		}
	}
}
