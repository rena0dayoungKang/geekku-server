package com.kosta.geekku.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.entity.Interior;
import com.kosta.geekku.entity.InteriorSample;
import com.kosta.geekku.entity.QInterior;
import com.kosta.geekku.entity.QInteriorSample;
import com.kosta.geekku.util.PageInfo;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class InteriorDslRepository {
	
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	public List<Interior> findInteriorListForMain() throws Exception {
		QInterior interior = QInterior.interior;
		return jpaQueryFactory.selectFrom(interior)
				.orderBy(interior.createdAt.desc())
				.limit(9)
				.fetch();
	}
	public List<InteriorSample> findSampleListForMain() throws Exception {
		QInteriorSample sample = QInteriorSample.interiorSample;
		return jpaQueryFactory.selectFrom(sample)
				.orderBy(sample.createdAt.desc())
				.limit(9)
				.fetch();
	}
	public List<InteriorDto> interiorList(PageInfo pageInfo, String area) throws Exception {
		return null;
	}
	
}
