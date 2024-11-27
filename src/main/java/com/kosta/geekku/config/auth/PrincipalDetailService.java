package com.kosta.geekku.config.auth;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kosta.geekku.entity.Company;
import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.CompanyRepository;
import com.kosta.geekku.repository.UserRepository;

@Service
public class PrincipalDetailService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		System.out.println("----------------------------------");
		System.out.println(username);
		
		Optional<User> ouser = userRepository.findByUsername(username);
		if(ouser.isPresent()) return new PrincipalDetails(ouser.get());	
		
		Optional<Company> ocompany = companyRepository.findByUsername(username);
		if(ocompany.isPresent()) return new PrincipalDetails(ocompany.get());
		
		//throw new UsernameNotFoundException("사용자 또는 회사를 찾을 수 없습니다: " + username);
		return null;
	}
	
	
}
