package com.fordogs.product.domain.specification;

import com.fordogs.product.domain.entity.ProductEntity;
import com.fordogs.user.domain.entity.UserEntity;
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
    private static final String PRODUCT_NAME = "name";

    public static Specification<ProductEntity> withSellerAndCategory(String sellerId, Category category, String productName) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (sellerId != null) {
                Join<ProductEntity, UserEntity> sellerJoin = root.join(SELLER);
                predicates.add(criteriaBuilder.equal(sellerJoin.get(ACCOUNT).get(VALUE), sellerId));
            }

            if (category != null) {
                predicates.add(criteriaBuilder.equal(root.get(CATEGORY), category));
            }

            if (productName != null) {
                predicates.add(criteriaBuilder.like(root.get(PRODUCT_NAME), "%" + productName + "%"));
            }

            predicates.add(criteriaBuilder.isTrue(root.get(ENABLED)));
            predicates.add(criteriaBuilder.isTrue(root.get(SELLER).get(ENABLED)));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
