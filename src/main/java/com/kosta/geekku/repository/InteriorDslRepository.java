package com.kosta.geekku.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.kosta.geekku.entity.Interior;
import com.kosta.geekku.entity.InteriorSample;
import com.kosta.geekku.entity.QInterior;
import com.kosta.geekku.entity.QInteriorBookmark;
import com.kosta.geekku.entity.QInteriorSample;
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
	public Long interiorCountAll() throws Exception {
		QInterior interior = QInterior.interior;
		return jpaQueryFactory.select(interior.count())
				.from(interior).fetchOne();
	}
	public Long interiorCountByLoc(String possibleLocation) throws Exception {
		QInterior interior = QInterior.interior;
		return jpaQueryFactory.select(interior.count())
				.from(interior)
				.where(interior.possibleLocation.eq(possibleLocation))
				.fetchOne();
	}
	public List<Interior> interiorListAll() throws Exception {
		QInterior interior = QInterior.interior;
		return jpaQueryFactory.selectFrom(interior)
				.orderBy(interior.createdAt.desc())
				.fetch();
	}
	public List<Interior> interiorListByLoc(String possibleLocation) throws Exception {
		QInterior interior = QInterior.interior;
		return jpaQueryFactory.selectFrom(interior)
				.where(interior.possibleLocation.eq(possibleLocation))
				.orderBy(interior.createdAt.desc())
				.fetch();
	}
	public Integer findInteriorBookmark(UUID userId, Integer interiorNum) throws Exception {
		QInteriorBookmark interiorBookmark = QInteriorBookmark.interiorBookmark;
		return jpaQueryFactory.select(interiorBookmark.bookmarkInteriorNum)
				.from(interiorBookmark)
				.where(interiorBookmark.userId.eq(userId).and(interiorBookmark.bookmarkInteriorNum.eq(interiorNum)))
				.fetchOne();
	}
}
