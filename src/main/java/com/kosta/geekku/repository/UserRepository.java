package com.kosta.geekku.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kosta.geekku.entity.User;

public interface UserRepository extends JpaRepository<User, UUID> {
	Optional<User> findByUsername(String username);
	Optional<User> findByNickname(String nickname);
	User findByProviderAndProviderId(String provider, String providerId);
	
}
