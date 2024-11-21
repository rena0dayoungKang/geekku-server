package com.kosta.geekku.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.geekku.entity.Estate;
import com.kosta.geekku.entity.QEstate;
import com.kosta.geekku.entity.QEstateBookmark;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class EstateDslRepository {
	
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	public Integer findBookmark(UUID userId, Integer estateNum) throws Exception {
		QEstateBookmark estateBookmark = QEstateBookmark.estateBookmark;
		
		return jpaQueryFactory.select(estateBookmark.bookmarkEstateNum)
					.from(estateBookmark)
					.where(estateBookmark.userId.eq(userId).and(estateBookmark.estateNum.eq(estateNum)))
					.fetchOne();
	}
	
	public Long findEstateCount() throws Exception {
		QEstate estate = QEstate.estate;
		
		return jpaQueryFactory.select(estate.count())
					.from(estate)
					.fetchOne();
	}
	
	public Long searchEstateCount(String type, String keyword) throws Exception {
		QEstate estate = QEstate.estate;
		Long cnt = 0L;

		if (type.equals("farmHouse")) {
			cnt = jpaQueryFactory.select(estate.count())
					.from(estate)
					.where(estate.type.eq(type).and(estate.address1.contains(keyword).or(estate.address2.contains(keyword))))
					.fetchOne();
		} else if (type.equals("countryHouse")) {
			cnt = jpaQueryFactory.select(estate.count())
					.from(estate)
					.where(estate.type.eq(type).and(estate.address1.contains(keyword).or(estate.address2.contains(keyword))))
					.fetchOne();
		} else if (type.equals("apt")) {
			cnt = jpaQueryFactory.select(estate.count())
					.from(estate)
					.where(estate.type.eq(type).and(estate.address1.contains(keyword).or(estate.address2.contains(keyword))))
					.fetchOne();
		} else if (type.equals("land")) {
			cnt = jpaQueryFactory.select(estate.count())
					.from(estate)
					.where(estate.type.eq(type).and(estate.address1.contains(keyword).or(estate.address2.contains(keyword))))
					.fetchOne();
		}
		
		return cnt;
	}
	
	public Long typeEstateCount(String type) throws Exception {
		QEstate estate = QEstate.estate;
		Long cnt = 0L;

		if (type.equals("farmHouse")) {
			cnt = jpaQueryFactory.select(estate.count())
					.from(estate)
					.where(estate.type.eq(type))
					.fetchOne();
		} else if (type.equals("countryHouse")) {
			cnt = jpaQueryFactory.select(estate.count())
					.from(estate)
					.where(estate.type.eq(type))
					.fetchOne();
		} else if (type.equals("apt")) {
			cnt = jpaQueryFactory.select(estate.count())
					.from(estate)
					.where(estate.type.eq(type))
					.fetchOne();
		} else if (type.equals("land")) {
			cnt = jpaQueryFactory.select(estate.count())
					.from(estate)
					.where(estate.type.eq(type))
					.fetchOne();
		}
		
		return cnt;
	}
	
	public List<Estate> findEstateListByPaging(PageRequest pageRequest) throws Exception {
		QEstate estate = QEstate.estate;
		
		return jpaQueryFactory.selectFrom(estate)
					.orderBy(estate.createdAt.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
	}
	
	public List<Estate> searchEstateListByPaging(PageRequest pageRequest, String type, String keyword) throws Exception {
		QEstate estate = QEstate.estate;
		List<Estate> estateList = null;

		if (type.equals("farmHouse") && keyword != null) {
			estateList = jpaQueryFactory.selectFrom(estate)
					.where(estate.type.eq(type).and(estate.address1.contains(keyword).or(estate.address2.contains(keyword))))
					.orderBy(estate.createdAt.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
		} else if (type.equals("countryHouse") && keyword != null) {
			estateList = jpaQueryFactory.selectFrom(estate)
					.where(estate.type.eq(type).and(estate.address1.contains(keyword).or(estate.address2.contains(keyword))))
					.orderBy(estate.createdAt.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
		} else if (type.equals("apt") && keyword != null) {
			estateList = jpaQueryFactory.selectFrom(estate)
					.where(estate.type.eq(type).and(estate.address1.contains(keyword).or(estate.address2.contains(keyword))))
					.orderBy(estate.createdAt.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
		} else if (type.equals("land") && keyword != null) {
			estateList = jpaQueryFactory.selectFrom(estate)
					.where(estate.type.eq(type).and(estate.address1.contains(keyword).or(estate.address2.contains(keyword))))
					.orderBy(estate.createdAt.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
		}
		
		return estateList;
	}
	
	public List<Estate> typeEstateListByPaging(PageRequest pageRequest, String type) throws Exception {
		QEstate estate = QEstate.estate;
		List<Estate> estateList = null;
		
		if (type.equals("farmHouse")) {
			estateList = jpaQueryFactory.selectFrom(estate)
					.where(estate.type.eq(type))
					.orderBy(estate.createdAt.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
		} else if (type.equals("countryHouse")) {
			estateList = jpaQueryFactory.selectFrom(estate)
					.where(estate.type.eq(type))
					.orderBy(estate.createdAt.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
		} else if (type.equals("apt")) {
			estateList = jpaQueryFactory.selectFrom(estate)
					.where(estate.type.eq(type))
					.orderBy(estate.createdAt.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
		} else if (type.equals("land")) {
			estateList = jpaQueryFactory.selectFrom(estate)
					.where(estate.type.eq(type))
					.orderBy(estate.createdAt.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
		}
		
		return estateList;
	}
	
	public List<Estate> findEstateListForMain() throws Exception {
		QEstate estate = QEstate.estate;
		
		return jpaQueryFactory.selectFrom(estate)
					.orderBy(estate.createdAt.desc())
					.limit(3)
					.fetch();
	}
}
