package com.kosta.geekku.repository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kosta.geekku.dto.MessageDto;
import com.kosta.geekku.entity.AlarmUser;

public interface AlarmUserRepository extends JpaRepository<AlarmUser, Integer> {

	List<AlarmUser> findByUser_UserIdAndTypeNotAndStatusFalse(UUID userId, String type);

	List<AlarmUser> findByCompany_CompanyIdAndTypeAndStatusFalse(UUID companyId, String type);
	
	List<AlarmUser> findByUser_UserIdAndTypeInAndStatusFalse(UUID userId, List<String> types);

	@Query("SELECT new com.kosta.geekku.dto.MessageDto("
	        + "a.userAlarmNum, a.message, a.user.userId, u.username, "
	        + "c.companyName, a.type, a.detailPath, a.createdAt, "
	        + "a.company.companyId, a.title) "
	        + "FROM AlarmUser a "
	        + "LEFT JOIN User u ON a.user.userId = u.userId "
	        + "LEFT JOIN Company c ON a.company.companyId = c.companyId "
	        + "WHERE a.user.userId = :userId AND a.status = false")
	List<MessageDto> findAlarmsWithUserAndCompany(@Param("userId") UUID userId);

}
