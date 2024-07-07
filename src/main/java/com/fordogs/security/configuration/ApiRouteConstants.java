package com.fordogs.security.configuration;

import com.fordogs.user.domain.enums.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiRouteConstants {

    public static final String ROLE_SELLER = Role.SELLER.name();
    public static final String ROLE_BUYER = Role.BUYER.name();

    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/swagger-ui/**",
            "/api-docs/**",
            "/docs",

            "/users/signup",
            "/users/login",

            "/products/*/details",
            "/products/search",

            "/health/status"
    );

    public static final List<String> MEMBER_ONLY_ENDPOINTS = List.of(
            "/users/deactivation",
            "/users/details",
            "/users/refresh",
            "/users/logout",

            "/health/token"
    );

    public static final List<String> BUYER_ONLY_ENDPOINTS = List.of(
            "/orders",
            "/orders/buyer",
            "/orders/cancel",

            "/payments"
    );

    public static final List<String> SELLER_ONLY_ENDPOINTS = List.of(
            "/products/register",
            "/products/images/upload",
            "/products/*/update",
            "/products/*/deactivate",
            "/products/images",

            "/orders/seller",
            "/orders/*/status"
    );
}
