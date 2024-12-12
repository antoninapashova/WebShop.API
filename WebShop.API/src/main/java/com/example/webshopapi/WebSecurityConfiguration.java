package com.example.webshopapi;

import com.example.webshopapi.config.CustomCorsConfiguration;
import com.example.webshopapi.filters.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtRequestFilter jwtFilter;

    @Autowired
    private CustomCorsConfiguration customCorsConfiguration;

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(customizer -> customizer.disable())
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/sign-up", "/authenticate", "/get-order/{orderId}", "/subscribe/{email}",
                                "/api/v1/auth/**",
                                "/v3/api-docs",
                                "/v2/api-docs",
                                "/swagger-resources/**",
                                "/swagger-ui/**",
                                "/webjars/**")
                        .permitAll()
                        .requestMatchers("/all-products", "/search/{name}").hasAnyRole("ADMIN", "CUSTOMER")
                        .requestMatchers("/add-product", "/all-orders",
                                "/set-order-approved", "/delete-product/{productId}",
                                "/update-product/{productId}", "/all-categories",
                                "/add-category/{categoryName}", "/set-order-status/{orderId}/{status}",
                                "/get-order-items/{orderId}", "/get-product/{productId}",
                                "/delete-image/{productId}/{imageId}", "/create-coupon",
                                "/all-coupons", "/order/analytics", "/create-promotion",
                                "/get-non-promotional-products")
                        .hasRole("ADMIN")
                        .requestMatchers("/add-to-cart", "/get-cart",
                                "/cart/changeItemQuantity", "/cart/setItemQuantity",
                                "/create-order", "/coupon/{code}", "/delete/{itemId}", "/user/orders")
                        .hasRole("CUSTOMER")
                        .anyRequest().authenticated())
                .cors(c -> c.configurationSource(customCorsConfiguration))
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager customizer(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}