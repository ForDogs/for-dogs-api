package com.fordogs.core.domian.specification;

import com.fordogs.core.domian.entity.ProductEntity;
import com.fordogs.core.domian.entity.UserManagementEntity;
import com.fordogs.core.domian.enums.Category;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductSpecification { // TODO: 추후 QueryDSL로 변경 예정, Entity 스펙 변경 시 자동으로 수정 불가

    public static Specification<ProductEntity> withSellerAndCategory(String sellerId, Category category) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (sellerId != null) {
                Join<ProductEntity, UserManagementEntity> sellerJoin = root.join("seller");
                predicates.add(criteriaBuilder.equal(sellerJoin.get("account").get("value"), sellerId));
            }

            if (category != null) {
                predicates.add(criteriaBuilder.equal(root.get("category"), category));
            }

            predicates.add(criteriaBuilder.isTrue(root.get("enabled")));
            predicates.add(criteriaBuilder.isTrue(root.get("seller").get("enabled")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
