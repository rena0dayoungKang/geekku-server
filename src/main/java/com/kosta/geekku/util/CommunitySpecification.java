package com.kosta.geekku.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
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

            // 주거 형태 (type)
            if (filter.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), filter.getType()));
            }

//            // 평수 (size) - 범위 필터링
//            if (filter.getSizeRange() != null) {
//                String[] sizeRange = filter.getSizeRange().split("-");
//                if (sizeRange.length == 2) {
//                    int minSize = Integer.parseInt(sizeRange[0]);
//                    if (sizeRange[1].equals("+")) {
//                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("size"), minSize));
//                    } else {
//                        int maxSize = Integer.parseInt(sizeRange[1]);
//                        predicates.add(criteriaBuilder.between(root.get("size"), minSize, maxSize));
//                    }
//                }
//            }
//
//            // 가족 형태 (familyType)
//            if (filter.getFamilyType() != null) {
//                predicates.add(criteriaBuilder.equal(root.get("familyType"), filter.getFamilyType()));
//            }

            // 스타일 (style)
            if (filter.getStyle() != null) {
                predicates.add(criteriaBuilder.equal(root.get("style"), filter.getStyle()));
            }

//            // 기간 (period) - 미리 정의된 옵션
//            if (filter.getPeriod() != null) {
//                LocalDate now = LocalDate.now();
//                LocalDate startDate = null;
//                switch (filter.getPeriod()) {
//                    case "1개월":
//                        startDate = now.minus(1, ChronoUnit.MONTHS);
//                        break;
//                    case "3개월":
//                        startDate = now.minus(3, ChronoUnit.MONTHS);
//                        break;
//                    case "6개월":
//                        startDate = now.minus(6, ChronoUnit.MONTHS);
//                        break;
//                    default:
//                        break;
//                }
//                if (startDate != null) {
//                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate"), startDate));
//                }
//            }
//
//            // 예산 (money) - 범위 필터링
//            if (filter.getMoneyRange() != null) {
//                String[] moneyRange = filter.getMoneyRange().split("-");
//                if (moneyRange.length == 2) {
//                    int minMoney = Integer.parseInt(moneyRange[0]);
//                    if (moneyRange[1].equals("+")) {
//                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("money"), minMoney));
//                    } else {
//                        int maxMoney = Integer.parseInt(moneyRange[1]);
//                        predicates.add(criteriaBuilder.between(root.get("money"), minMoney, maxMoney));
//                    }
//                }
//            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
