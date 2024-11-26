package com.kosta.geekku.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.kosta.geekku.entity.InteriorSample;
import com.kosta.geekku.entity.QInteriorSample;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class InteriorSampleDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	public List<InteriorSample> interiorSampleListByPaging(Pageable pageable) {
		QInteriorSample interiorSample = QInteriorSample.interiorSample;
		return jpaQueryFactory.selectFrom(interiorSample).orderBy(interiorSample.sampleNum.desc())
				.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch();
	}
}
