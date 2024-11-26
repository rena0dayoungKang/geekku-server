package com.kosta.geekku.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.geekku.entity.InteriorRequest;
import com.kosta.geekku.entity.QInteriorRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class InteriorRequestDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	public Long interiorRequestCount() throws Exception {
		QInteriorRequest interiorRequest = QInteriorRequest.interiorRequest;

		return jpaQueryFactory.select(interiorRequest.count()).from(interiorRequest).fetchOne();
	}

	public List<InteriorRequest> interiorRequestListByPaging(PageRequest pageRequest) throws Exception {
		QInteriorRequest interiorRequest = QInteriorRequest.interiorRequest;

		List<InteriorRequest> interiorRequestDtoList = jpaQueryFactory.selectFrom(interiorRequest)
				.orderBy(interiorRequest.createdAt.asc()).offset(pageRequest.getOffset())
				.limit(pageRequest.getPageSize()).fetch();

		return interiorRequestDtoList;
	}
}
