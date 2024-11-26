package com.kosta.geekku.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.geekku.entity.InteriorAllAnswer;
import com.kosta.geekku.entity.InteriorAllRequest;
import com.kosta.geekku.entity.QInteriorAllAnswer;
import com.kosta.geekku.entity.QInteriorAllRequest;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class InteriorAllRequestDslRepository {
	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	// 조회수
	public Long findInteriorAllCount() throws Exception {
		QInteriorAllRequest interiorAll = QInteriorAllRequest.interiorAllRequest;
		return jpaQueryFactory.select(interiorAll.count()).from(interiorAll).fetchOne();
	}

	// 리스트 페이징처리
	public List<InteriorAllRequest> findInteriorAllListByPaging(PageRequest pageRequest) throws Exception {
		QInteriorAllRequest interiorAll = QInteriorAllRequest.interiorAllRequest;
		return jpaQueryFactory.selectFrom(interiorAll).orderBy(interiorAll.requestAllNum.desc())
				.offset(pageRequest.getOffset()).limit(pageRequest.getPageSize()).fetch();

	}

	public Long searchInteriorAllCount(String type, String word) throws Exception {
		QInteriorAllRequest interiorAll = QInteriorAllRequest.interiorAllRequest;
		Long cnt = 0L;
		if (type.equals("title")) {
			cnt = jpaQueryFactory.select(interiorAll.count()).from(interiorAll).where(interiorAll.title.contains(word))
					.fetchOne();
		} else if (type.equals("address1")) {
			cnt = jpaQueryFactory.select(interiorAll.count()).from(interiorAll)
					.where(interiorAll.address1.contains(word)).fetchOne();
		} else if (type.equals("interiorType")) {
			cnt = jpaQueryFactory.select(interiorAll.count()).from(interiorAll)
					.where(interiorAll.interiorType.contains(word)).fetchOne();
		}
		return cnt;
	}

	// 제목, 지역, 시공타입
	public List<InteriorAllRequest> searchInteriorAllListByPaging(PageRequest pageRequest, String type, String word)
			throws Exception {
		QInteriorAllRequest interiorAll = QInteriorAllRequest.interiorAllRequest;
		List<InteriorAllRequest> interiorAllList = null;
		if (type.equals("title")) {
			interiorAllList = jpaQueryFactory.selectFrom(interiorAll).where(interiorAll.title.contains(word))
					.orderBy(interiorAll.requestAllNum.desc()).offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize()).fetch();
		} else if (type.equals("address1")) {
			interiorAllList = jpaQueryFactory.selectFrom(interiorAll).where(interiorAll.address1.contains(word))
					.orderBy(interiorAll.requestAllNum.desc()).offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize()).fetch();
		} else if (type.equals("interiorType")) {
			interiorAllList = jpaQueryFactory.selectFrom(interiorAll).where(interiorAll.interiorType.contains(word))
					.orderBy(interiorAll.requestAllNum.desc()).offset(pageRequest.getOffset())
					.limit(pageRequest.getPageSize()).fetch();
		}
		return interiorAllList;
	}

	// 방꾸 답변
	public Long interiorAllAnswerCount() throws Exception {
		QInteriorAllAnswer interiorAllAnswer = QInteriorAllAnswer.interiorAllAnswer;

		return jpaQueryFactory.select(interiorAllAnswer.count()).from(interiorAllAnswer).fetchOne();
	}

	public List<InteriorAllAnswer> interiorAllAnswerListByPaging(PageRequest pageRequest) throws Exception {
		QInteriorAllAnswer interiorAllAnswer = QInteriorAllAnswer.interiorAllAnswer;

		List<InteriorAllAnswer> interiorAllAnswerList = jpaQueryFactory.selectFrom(interiorAllAnswer)
				.orderBy(interiorAllAnswer.createdAt.asc()).offset(pageRequest.getOffset())
				.limit(pageRequest.getPageSize()).fetch();

		return interiorAllAnswerList;
	}
}
