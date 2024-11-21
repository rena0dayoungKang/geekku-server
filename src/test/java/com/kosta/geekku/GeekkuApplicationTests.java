package com.kosta.geekku;

import java.util.List;

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
		List<User> userList = userRepository.findAll();
		System.out.println(userList);
	}
	
	@Test
	void selectCompany() {
		List<Company> comList = companyRepository.findAll();
		System.out.println(comList);
	}

}
