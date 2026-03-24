package com.kosta.geekku.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import com.kosta.geekku.dto.InteriorDto;
import com.kosta.geekku.dto.SampleDto;
import com.kosta.geekku.entity.Interior;
import com.kosta.geekku.entity.InteriorSample;
import com.kosta.geekku.entity.QCompany;
import com.kosta.geekku.entity.QInterior;
import com.kosta.geekku.entity.QInteriorBookmark;
import com.kosta.geekku.entity.QInteriorSample;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections; //인테리어 메인에서 불필요한 인테리어,샘플 데이터 정리
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

@Repository
public class InteriorDslRepository {

	@Autowired
	private JPAQueryFactory jpaQueryFactory;

	//필요한 데이터만 가져오기 위해 밑으로 변경
//	public List<Interior> findInteriorListForMain() throws Exception {
//		QInterior interior = QInterior.interior;
//		return jpaQueryFactory.selectFrom(interior).orderBy(interior.createdAt.desc()).limit(9).fetch();
//	}

	public List<InteriorDto> findInteriorListForMain() throws Exception {
	    QInterior interior = QInterior.interior;
	    QCompany company = QCompany.company; // 업체명(companyName)을 가져오기 위한 조인용

	    // 1. DB에서 13MB짜리 사진(coverImage)은 빼고, 필요한 글자만 가볍게 뽑아서 DTO에 담습니다.
	    List<InteriorDto> dtoList = jpaQueryFactory
	            .select(Projections.fields(InteriorDto.class,
	                    interior.interiorNum,
	                    interior.possiblePart,
	                    interior.period,
	                    interior.recentCount,
	                    interior.repairDate,
	                    interior.possibleLocation,
	                    interior.intro,
	                    interior.content,
	                    interior.createdAt,
	                    
	                    // N+1 문제 해결: 조인된 테이블에서 업체 정보도 한 번에 싹 가져옵니다.
	                    company.companyName,
	                    company.companyId,
	                    company.regStatus
	            ))
	            .from(interior)
	            .leftJoin(interior.company, company) // QCompany와 조인!
	            .orderBy(interior.createdAt.desc())
	            .limit(9)
	            .fetch();

	    // 2. 가볍게 가져온 DTO 리스트에 프론트엔드가 쓸 '이미지 URL'만 텍스트로 세팅해 줍니다.
	    for (InteriorDto dto : dtoList) {
	        dto.setCoverImageStr("http://localhost:8080/interior/image/" + dto.getInteriorNum());
	    }

	    return dtoList;
	}
	
// 	필요한 데이터만 가져오기 위해 밑으로 변경
//	public List<InteriorSample> findSampleListForMain() throws Exception {
//		QInteriorSample sample = QInteriorSample.interiorSample;
//		return jpaQueryFactory.selectFrom(sample).orderBy(sample.createdAt.desc()).limit(9).fetch();
//	}
	
	public List<SampleDto> findSampleListForMain() throws Exception {
		QInteriorSample sample = QInteriorSample.interiorSample;
		QInterior interior = QInterior.interior;
		QCompany company = QCompany.company;
		
		return jpaQueryFactory
				.select(Projections.fields(SampleDto.class,
						sample.sampleNum,
						interior.interiorNum,
						sample.title,
						sample.coverImage,
						
						company.companyName,
						company.companyId
						))
				.from(sample)
				.leftJoin(sample.interior, interior)
				.leftJoin(interior.company, company)
				.orderBy(sample.createdAt.desc())
				.limit(9)
				.fetch();
	}


	public Long interiorCountAll() throws Exception {
		QInterior interior = QInterior.interior;
		return jpaQueryFactory.select(interior.count()).from(interior).fetchOne();
	}

	public Long interiorCountByLoc(String possibleLocation) throws Exception {
		QInterior interior = QInterior.interior;
		return jpaQueryFactory.select(interior.count())
				.from(interior)
				.where(interior.possibleLocation.contains(possibleLocation))
				.fetchOne();
	}

	public List<Interior> interiorListAll(Integer offset, Integer limit) throws Exception {
		QInterior interior = QInterior.interior;
		return jpaQueryFactory.selectFrom(interior)
				.orderBy(interior.createdAt.desc())
				.offset(offset)
				.limit(limit)
				.fetch();
	}

	public List<Interior> interiorListByLoc(String possibleLocation, Integer offset, Integer limit) throws Exception {
		QInterior interior = QInterior.interior;
		return jpaQueryFactory.selectFrom(interior)
				.where(interior.possibleLocation.contains(possibleLocation))
				.orderBy(interior.createdAt.desc())
				.offset(offset)
				.limit(limit)
				.fetch();
	}

	public Integer findInteriorBookmark(UUID userId, Integer interiorNum) throws Exception {
		QInteriorBookmark interiorBookmark = QInteriorBookmark.interiorBookmark;

		return jpaQueryFactory.select(interiorBookmark.bookmarkInteriorNum)
				.from(interiorBookmark)
				.where(interiorBookmark.userId.eq(userId).and(interiorBookmark.interior.interiorNum.eq(interiorNum)))
				.fetchOne();
	}

	public List<InteriorSample> sampleListByFilter(String date, String[] type, String[] style, Integer[] size,
			String[] location) {
		QInteriorSample sample = QInteriorSample.interiorSample;
		BooleanBuilder filter = new BooleanBuilder();
//		if (date != null) {
//			filter.and(sample.createdAt.eq(date));
//		}
		if (type != null && type.length > 0) {
			filter.and(sample.type.in(type));
		}
		if (style != null && style.length > 0) {
			filter.and(sample.style.in(style));
		}
		if (size != null && size.length > 0) {
			filter.and(sample.size.in(size));
		}
		if (location != null && location.length > 0) {
			filter.and(sample.location.in(location));
		}

		JPAQuery<InteriorSample> query = jpaQueryFactory.selectFrom(sample).where(filter);
		if (date != null) {
			if ("latest".equalsIgnoreCase(date)) {
				query.orderBy(sample.createdAt.desc());
			} else if ("oldest".equalsIgnoreCase(date)) {
				query.orderBy(sample.createdAt.asc());
			}
		}
		return query.fetch();
	}

	public Long sampleCountByFilter(String date, String[] type, String[] style, Integer[] size, String[] location)
			throws Exception {
		QInteriorSample sample = QInteriorSample.interiorSample;
		BooleanBuilder filter = new BooleanBuilder();

		if (type != null && type.length > 0) {
			filter.and(sample.type.in(type));
		}
		if (style != null && style.length > 0) {
			filter.and(sample.style.in(style));
		}
		if (size != null && size.length > 0) {
			filter.and(sample.size.in(size));
		}
		if (location != null && location.length > 0) {
			filter.and(sample.location.in(location));
		}

		return jpaQueryFactory.select(sample.count()).from(sample).where(filter).fetchOne();
	}

	public List<InteriorSample> interiorSampleListmypage(PageRequest pageRequest, UUID companyId) throws Exception {
		QInteriorSample interiorSample = QInteriorSample.interiorSample;
		return jpaQueryFactory.selectFrom(interiorSample).where(interiorSample.company.companyId.eq(companyId))
				.orderBy(interiorSample.createdAt.desc()).offset(pageRequest.getOffset())
				.limit(pageRequest.getPageSize()).fetch(); // 무한스크롤 갯수 제한
	}

	/*
	 * public List<InteriorSample> interiorReviewListmypage(PageRequest pageRequest,
	 * UUID companyId) throws Exception { QInteriorRequest interiorRequest =
	 * QInteriorSample.interiorSample; return
	 * jpaQueryFactory.selectFrom(interiorSample).where(interiorSample.company.
	 * companyId.eq(companyId)) .orderBy(interiorSample.createdAt.desc()).fetch(); }
	 */

	/*
	 * public List<InteriorRequest> interiorRequestList(PageRequest pageRequest,
	 * UUID companyId) throws Exception { QInteriorRequest interiorRequest =
	 * QInteriorRequest.interiorRequest; return
	 * jpaQueryFactory.selectFrom(interiorRequest).where(interiorRequest.company.
	 * companyId.eq(companyId)) .orderBy(interiorRequest.createdAt.desc()).fetch();
	 * }
	 */

	public Long findMypageEstateCount(UUID companyId) throws Exception {
		QInteriorSample interiorsample = QInteriorSample.interiorSample;

		return jpaQueryFactory.select(interiorsample.count()).from(interiorsample)
				.where(interiorsample.company.companyId.eq(companyId)).fetchOne();
	}

}
