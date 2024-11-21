package com.kosta.geekku.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.geekku.entity.House;
import com.kosta.geekku.entity.QHouse;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class HouseDslRepository {
	
	@Autowired
	private JPAQueryFactory jpaQueryFactory;
	
	public Long findHouseCount() throws Exception {
		QHouse house = QHouse.house;
		 
		return jpaQueryFactory.select(house.count())
					.from(house)
					.fetchOne();
	}
	
	public Long searchHouseCount(String type, String keyword) throws Exception {
		QHouse house = QHouse.house;
		Long cnt = 0L;
		
		if (type.equals("location")) {
			cnt = jpaQueryFactory.select(house.count())
					.from(house)
					.where(house.address1.contains(keyword).or(house.address2.contains(keyword)))
					.fetchOne();
		}
		else if (type.equals("rentType")) {
			cnt = jpaQueryFactory.select(house.count())
					.from(house)
					.where(house.rentType.contains(keyword))
					.fetchOne();
		} else if (type.equals("title")) {
			cnt = jpaQueryFactory.select(house.count())
					.from(house)
					.where(house.title.contains(keyword))
					.fetchOne();
		}
		
		return cnt;
	}
	
	public List<House> findHouseListByPaging(PageRequest pageRequest) throws Exception {
		QHouse house = QHouse.house;
		
		return jpaQueryFactory.selectFrom(house)
					.orderBy(house.createdAt.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
	}
	
	
	public List<House> searchHouseListByPaging(PageRequest pageRequest, String type, String keyword) throws Exception {
		QHouse house = QHouse.house;
		List<House> houseList = null;
		
		if (type.equals("location")) {
			houseList = jpaQueryFactory.selectFrom(house)
					.where(house.address1.contains(keyword).or(house.address2.contains(keyword)))
					.orderBy(house.createdAt.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
		} else if (type.equals("rentType")) {
			houseList = jpaQueryFactory.selectFrom(house)
					.where(house.rentType.contains(keyword))
					.orderBy(house.createdAt.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
		} else if (type.equals("title")) {
			houseList = jpaQueryFactory.selectFrom(house)
					.where(house.title.contains(keyword))
					.orderBy(house.createdAt.desc())
					.offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize())
					.fetch();
		}
		
		return houseList;
	}
}