package com.fordogs.configuraion;

import com.fordogs.core.domian.enums.Role;
import com.fordogs.core.util.PasswordUtil;
import com.fordogs.security.filter.JwtAuthenticationFilter;
import com.fordogs.security.handler.CustomAccessDeniedHandler;
import com.fordogs.security.handler.CustomAuthenticationEntryPoint;
import com.fordogs.security.provider.CustomUserDetailsServiceProvider;
import com.fordogs.security.provider.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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

    private static final String ROLE_SELLER = Role.SELLER.name();
    private static final String ROLE_BUYER = Role.BUYER.name();

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomUserDetailsServiceProvider userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        http.sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.cors(corsConfig -> corsConfig
                .configurationSource(corsConfigurationSource())
        );

        http.authorizeHttpRequests(authorizeRequest -> authorizeRequest
                .requestMatchers(HttpMethod.DELETE, "/users/deactivation").authenticated()

                .requestMatchers(HttpMethod.POST, "/products/**").hasAuthority(ROLE_SELLER)

                .requestMatchers("/swagger-ui/**", "/api-docs/**", "/docs").permitAll()
                .requestMatchers("/users/**").permitAll()
                .requestMatchers("/products/**").permitAll()
                .requestMatchers("/test/request").permitAll()
                .anyRequest().authenticated()
        );

        http.exceptionHandling(exceptionHandlingConfigurer -> exceptionHandlingConfigurer
                .authenticationEntryPoint(customAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler)
        );

        http.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(PasswordUtil.passwordEncoder());

        return authenticationProvider;
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
