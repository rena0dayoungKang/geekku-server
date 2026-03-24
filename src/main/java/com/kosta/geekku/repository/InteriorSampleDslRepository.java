package com.kosta.geekku.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.kosta.geekku.entity.InteriorSample;
import com.kosta.geekku.entity.QInteriorSample;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
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
	
	// ✨ [신규 추가] 다중 필터링 & 페이징을 처리하는 동적 쿼리 마법사!
    public Page<InteriorSample> findFilteredSamples(
            String date, String[] types, String[] styles, List<Integer> sizes, String[] location, Pageable pageable) {
        
        QInteriorSample interiorSample = QInteriorSample.interiorSample;
        BooleanBuilder builder = new BooleanBuilder();

        // 1. 프론트에서 넘어온 조건이 있을 때만 WHERE 절에 합체시킵니다.
        if (types != null && types.length > 0) {
            builder.and(interiorSample.type.in(types));
        }
        if (styles != null && styles.length > 0) {
            builder.and(interiorSample.style.in(styles));
        }
        if (sizes != null && !sizes.isEmpty()) {
            builder.and(interiorSample.size.in(sizes));
        }
        if (location != null && location.length > 0) {
            builder.and(interiorSample.location.in(location)); 
        }

        // 2. 정렬 기준 (최신순 vs 과거순)
        OrderSpecifier<?> orderSpecifier = "oldest".equals(date) ? 
                interiorSample.createdAt.asc() : interiorSample.createdAt.desc();

        // 3. 조건에 맞는 데이터 목록 조회 (LIMIT 적용)
        List<InteriorSample> content = jpaQueryFactory
                .selectFrom(interiorSample)
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset()) 
                .limit(pageable.getPageSize()) 
                .fetch();

        // 4. 전체 데이터 개수 조회 (페이징 버튼을 만들기 위해 필수!)
        Long totalCount = jpaQueryFactory
                .select(interiorSample.count())
                .from(interiorSample)
                .where(builder)
                .fetchOne();

        // 5. 예쁘게 Page 객체로 포장해서 Service로 넘겨줍니다.
        return new PageImpl<>(content, pageable, totalCount == null ? 0 : totalCount);
    }
}
