package com.fordogs.product.domain.specification;

import com.fordogs.product.domain.entity.ProductEntity;
import com.fordogs.user.domain.entity.UserManagementEntity;
import com.fordogs.product.domain.enums.Category;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductSpecification {

    private static final String SELLER = "seller";
    private static final String ACCOUNT = "account";
    private static final String VALUE = "value";
    private static final String CATEGORY = "category";
    private static final String ENABLED = "enabled";

    public static Specification<ProductEntity> withSellerAndCategory(String sellerId, Category category) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (sellerId != null) {
                Join<ProductEntity, UserManagementEntity> sellerJoin = root.join(SELLER);
                predicates.add(criteriaBuilder.equal(sellerJoin.get(ACCOUNT).get(VALUE), sellerId));
            }

            if (category != null) {
                predicates.add(criteriaBuilder.equal(root.get(CATEGORY), category));
            }

            predicates.add(criteriaBuilder.isTrue(root.get(ENABLED)));
            predicates.add(criteriaBuilder.isTrue(root.get(SELLER).get(ENABLED)));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
