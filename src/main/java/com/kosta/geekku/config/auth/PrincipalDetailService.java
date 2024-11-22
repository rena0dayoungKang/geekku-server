package com.kosta.geekku.config.auth;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kosta.geekku.entity.User;
import com.kosta.geekku.repository.UserRepository;

@Service
public class PrincipalDetailService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("----------------------------------");
		System.out.println(username);
		Optional<User> ouser = userRepository.findByUsername(username);
		if(ouser.isPresent()) return new PrincipalDetails(ouser.get());	
		return null;
	}
	
	
}
