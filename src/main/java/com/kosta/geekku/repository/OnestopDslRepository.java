package com.kosta.geekku.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.geekku.entity.Onestop;
import com.kosta.geekku.entity.QOnestop;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class OnestopDslRepository {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	// 조회수
	public Long findOnestopCount() throws Exception {
		QOnestop onestop = QOnestop.onestop;
		return jpaQueryFactory.select(onestop.count()).from(onestop).fetchOne();
	}

	// 리스트 페이징처리
	public List<Onestop> findOnestopListByPaging(PageRequest pageRequest) throws Exception {
		QOnestop onestop = QOnestop.onestop;
		return jpaQueryFactory.selectFrom(onestop).orderBy(onestop.onestopNum.desc()).offset(pageRequest.getOffset())
				.limit(pageRequest.getPageSize()).fetch();
	}

	public Long searchOnestopCount(String type, String word) throws Exception {
		QOnestop onestop = QOnestop.onestop;
		Long cnt = 0L;
		if (type.equals("title")) {
			cnt = jpaQueryFactory.select(onestop.count()).from(onestop).where(onestop.title.contains(word)).fetchOne();
		} else if (type.equals("content")) {
			cnt = jpaQueryFactory.select(onestop.count()).from(onestop).where(onestop.content.contains(word))
					.fetchOne();
		} else if (type.equals("writer")) {
			cnt = jpaQueryFactory.select(onestop.count()).from(onestop).where(onestop.user.nickname.contains(word))
					.fetchOne();
		}
		return cnt;
	}

	// 제목 내용 작성자로 검색 -> 작성자 대신 지역으로 변경 예정
	public List<Onestop> searchOnestopListByPaging(PageRequest pageRequest, String type, String word) throws Exception {
		QOnestop onestop = QOnestop.onestop;
		List<Onestop> onestopList = null;
		if (type.equals("title")) {
			onestopList = jpaQueryFactory.selectFrom(onestop).where(onestop.title.contains(word))
					.orderBy(onestop.onestopNum.desc()).offset(pageRequest.getOffset()).limit(pageRequest.getPageSize())
					.fetch();
		} else if (type.equals("content")) {
			onestopList = jpaQueryFactory.selectFrom(onestop).where(onestop.content.contains(word))
					.orderBy(onestop.onestopNum.desc()).offset(pageRequest.getOffset()).limit(pageRequest.getPageSize())
					.fetch();
		} else if (type.equals("writer")) {
			onestopList = jpaQueryFactory.selectFrom(onestop).where(onestop.user.nickname.contains(word))
					.orderBy(onestop.onestopNum.desc()).offset(pageRequest.getOffset()).limit(pageRequest.getPageSize())
					.fetch();
		}
		return onestopList;
	}

	/*
	 * public void updateOnestopViewCount(Integer onestopNum, Integer viewCount)
	 * throws Exception { QOnestop onestop = QOnestop.onestop;
	 * jpaQueryFactory.update(onestop).set(onestop.viewCount,
	 * viewCount).where(onestop.onestopNum.eq(onestopNum)) .execute(); }
	 */
}
