package com.kosta.geekku.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.geekku.entity.InteriorAllAnswer;
import com.kosta.geekku.entity.Onestop;
import com.kosta.geekku.entity.OnestopAnswer;
import com.kosta.geekku.entity.QInteriorAllAnswer;
import com.kosta.geekku.entity.QOnestop;
import com.kosta.geekku.entity.QOnestopAnswer;
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
		} else if (type.equals("rentType")) {
			cnt = jpaQueryFactory.select(onestop.count()).from(onestop).where(onestop.rentType.contains(word))
					.fetchOne();
		} else if (type.equals("address1")) {
			cnt = jpaQueryFactory.select(onestop.count()).from(onestop).where(onestop.address1.contains(word))
					.fetchOne();
		}
		return cnt;
	}

	// 제목, 지역, 거래타입
	public List<Onestop> searchOnestopListByPaging(PageRequest pageRequest, String type, String keyword)
			throws Exception {
		QOnestop onestop = QOnestop.onestop;
		List<Onestop> onestopList = null;
		if (type.equals("title")) {
			onestopList = jpaQueryFactory.selectFrom(onestop).where(onestop.title.contains(keyword))
					.orderBy(onestop.onestopNum.desc()).offset(pageRequest.getOffset()).limit(pageRequest.getPageSize())
					.fetch();
		} else if (type.equals("rentType")) {
			onestopList = jpaQueryFactory.selectFrom(onestop).where(onestop.rentType.contains(keyword))
					.orderBy(onestop.onestopNum.desc()).offset(pageRequest.getOffset()).limit(pageRequest.getPageSize())
					.fetch();
		} else if (type.equals("address1")) {
			onestopList = jpaQueryFactory.selectFrom(onestop).where(onestop.address1.contains(keyword))
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

	// 방꾸 답변
	public Long onestopAnswerCount() throws Exception {
		QOnestopAnswer onestopAnswer = QOnestopAnswer.onestopAnswer;

		return jpaQueryFactory.select(onestopAnswer.count()).from(onestopAnswer).fetchOne();
	}

	public List<OnestopAnswer> onestopAnswerListByPaging(PageRequest pageRequest, Integer onestopNum) throws Exception {
		QOnestopAnswer onestopAnswer = QOnestopAnswer.onestopAnswer;

		List<OnestopAnswer> onestopAnswerList = jpaQueryFactory.selectFrom(onestopAnswer)
				.where(onestopAnswer.onestop.onestopNum.eq(onestopNum)).orderBy(onestopAnswer.createdAt.asc())
				.offset(pageRequest.getOffset()).limit(pageRequest.getPageSize()).fetch();

		return onestopAnswerList;
	}

	public void updateOnestopViewCount(Integer onestopNum, Integer viewCount) throws Exception {
		QOnestop onestop = QOnestop.onestop;

		jpaQueryFactory.update(onestop).set(onestop.viewCount, viewCount).where(onestop.onestopNum.eq(onestopNum))
				.execute();

	}
}
