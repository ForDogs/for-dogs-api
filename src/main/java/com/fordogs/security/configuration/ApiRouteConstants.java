package com.fordogs.security.configuration;

import com.fordogs.user.domain.enums.Role;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpMethod;

import java.util.Map;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiRouteConstants {

    public static final String ROLE_SELLER = Role.SELLER.name();
    public static final String ROLE_BUYER = Role.BUYER.name();

    public static final Map<String, HttpMethod> PUBLIC_ENDPOINTS = Map.of(
            "/swagger-ui/**", HttpMethod.GET,
            "/api-docs/**", HttpMethod.GET,
            "/docs", HttpMethod.GET,

            "/users", HttpMethod.POST,
            "/users/login",  HttpMethod.POST,

            "/products/*",  HttpMethod.GET,
            "/products",  HttpMethod.GET,

            "/health/status",  HttpMethod.GET
    );

    public static final Map<String, Set<HttpMethod>> MEMBER_ONLY_ENDPOINTS = Map.of(
            "/users/profile", Set.of(HttpMethod.DELETE, HttpMethod.GET),
            "/users/refresh", Set.of(HttpMethod.POST),
            "/users/logout", Set.of(HttpMethod.POST),

            "/health/token", Set.of(HttpMethod.GET),

            "/payments/*", Set.of(HttpMethod.GET)
    );

    public static final Map<String, HttpMethod> BUYER_ONLY_ENDPOINTS = Map.of(
            "/orders", HttpMethod.POST,
            "/orders/*/cancel", HttpMethod.POST,
            "/orders/buyer", HttpMethod.GET,

            "/payments", HttpMethod.POST,

            "/carts", HttpMethod.POST
    );

    public static final Map<String, Set<HttpMethod>> SELLER_ONLY_ENDPOINTS = Map.of(
            "/products", Set.of(HttpMethod.POST),
            "/products/*", Set.of(HttpMethod.PATCH, HttpMethod.DELETE),
            "/products/images", Set.of(HttpMethod.POST, HttpMethod.DELETE),

            "/orders/seller", Set.of(HttpMethod.GET),
            "/orders/*/status", Set.of(HttpMethod.PATCH)
    );
}
