package com.fordogs.security.configuration;


import com.fordogs.security.application.CustomOAuth2UserService;
import com.fordogs.security.filter.JwtAuthenticationFilter;
import com.fordogs.security.handler.CustomAccessDeniedHandler;
import com.fordogs.security.handler.CustomAuthenticationEntryPoint;
import com.fordogs.security.handler.CustomOAuth2FailureHandler;
import com.fordogs.security.handler.CustomOAuth2SuccessHandler;
import com.fordogs.security.infrastructure.HttpCookieOAuth2AuthorizationRequestRepository;
import com.fordogs.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtUtil jwtUtil;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOAuth2SuccessHandler customOAuth2SuccessHandler;
    private final CustomOAuth2FailureHandler customOAuth2FailureHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/error", "/favicon.ico");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.cors(corsConfig -> corsConfig
                .configurationSource(corsConfigurationSource())
        );

        http.authorizeHttpRequests(authorizeRequest -> {
            ApiRouteConstants.SWAGGER_ENDPOINTS.forEach((path, method) ->
                    authorizeRequest.requestMatchers(method, path).permitAll()
            );

            ApiRouteConstants.PUBLIC_ENDPOINTS.forEach((path, method) ->
                    authorizeRequest.requestMatchers(method, path).permitAll()
            );

            ApiRouteConstants.MEMBER_ONLY_ENDPOINTS.forEach((path, methods) ->
                    methods.forEach(method ->
                            authorizeRequest.requestMatchers(method, path).authenticated()
                    )
            );

            ApiRouteConstants.BUYER_ONLY_ENDPOINTS.forEach((path, methods) ->
                    methods.forEach(method ->
                            authorizeRequest.requestMatchers(method, path).hasAuthority(ApiRouteConstants.ROLE_BUYER)
                    )
            );

            ApiRouteConstants.SELLER_ONLY_ENDPOINTS.forEach((path, methods) ->
                    methods.forEach(method ->
                            authorizeRequest.requestMatchers(method, path).hasAuthority(ApiRouteConstants.ROLE_SELLER)
                    )
            );

            authorizeRequest.anyRequest().authenticated();
        });

        // Redirect URI: {baseUrl}/login/oauth2/code/{registrationId}
        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userinfo -> userinfo.userService(customOAuth2UserService))
                .successHandler(customOAuth2SuccessHandler)
                .failureHandler(customOAuth2FailureHandler)
                .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
                        .authorizationRequestRepository(httpCookieOAuth2AuthorizationRequestRepository)
                )
        );

        http.exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
        );

        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtUtil);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
