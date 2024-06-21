package com.fordogs.security.configuration;

import com.fordogs.user.domain.enums.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiRouteConstants {

    public static final String ROLE_SELLER = Role.SELLER.name();

    public static final List<String> PUBLIC_ENDPOINTS = List.of(
            "/swagger-ui/**",
            "/api-docs/**",
            "/docs",

            "/users/signup",
            "/users/login",
            "/user/refresh",

            "/products/*/details",
            "/products/search",

            "/health/status"
    );

    public static final List<String> MEMBER_ONLY_ENDPOINTS = List.of(
            "/users/deactivation",
            "/users/details",

            "/orders",

            "/health/token"
    );

    public static final List<String> SELLER_ONLY_ENDPOINTS = List.of(
            "/products/register",
            "/products/images/upload",
            "/products/*/update",
            "/products/*/deactivate",
            "/products/images"
    );
}
