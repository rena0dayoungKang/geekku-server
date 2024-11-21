package com.kosta.geekku.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.kosta.geekku.dto.CommunityFilterDto;
import com.kosta.geekku.entity.Community;

public class CommunitySpecification {

    public static Specification<Community> filterBy(CommunityFilterDto filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 주거형태 (type)
            if (filter.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), filter.getType()));
            }

            // 평수 (size)
            if (filter.getSize() != null) {
                predicates.add(criteriaBuilder.equal(root.get("size"), filter.getSize()));
            }

            // 스타일 (style)
            if (filter.getStyle() != null) {
                predicates.add(criteriaBuilder.equal(root.get("style"), filter.getStyle()));
            }

            // 기간 (period_start, period_end)
            if (filter.getPeriodStart() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("periodStart"), filter.getPeriodStart()));
            }
            if (filter.getPeriodEnd() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("periodEnd"), filter.getPeriodEnd()));
            }

            // 예산 (money)
            if (filter.getMoney() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("money"), filter.getMoney()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
