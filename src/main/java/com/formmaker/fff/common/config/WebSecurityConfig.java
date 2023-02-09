package com.formmaker.fff.common.config;

import com.formmaker.fff.common.exception.CustomAuthenticationEntryPoint;
import com.formmaker.fff.common.jwt.JwtAuthFilter;
import com.formmaker.fff.common.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity // 스프링 Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final JwtUtil jwtUtil;

    @Bean // 비밀번호 암호화 기능 등록
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .cors().configurationSource(corsConfigurationSource());

        http.httpBasic().authenticationEntryPoint(authenticationEntryPoint());

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/api/user/**").permitAll()
                .antMatchers("/api/refresh").permitAll()
                .antMatchers(HttpMethod.GET, "/api/survey").permitAll()
                .antMatchers(HttpMethod.GET,"/api/question").permitAll()
                .antMatchers(HttpMethod.POST, "/api/survey/**/reply").permitAll()
                .antMatchers(HttpMethod.GET, "/api/survey/main").permitAll()
                .antMatchers(HttpMethod.GET, "/api/survey/stats/download/**").permitAll()
                /*swagger*/
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                /* sse */
                .antMatchers("/api/sse/**").permitAll()
                /* certbot */
                .antMatchers("/.well-known/acme-challenge/83uJnLcbeDXcIbvL3Tv8WrLtVqvz4kr5A12i5E2IsPU").permitAll()
                .antMatchers("/.well-known/acme-challenge/KlMCBPNioLlCM0x08hj7nkV8nJ53ibdKXY2lEYQMu1A").permitAll()
                .anyRequest().authenticated();

        http.addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // cors 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("https://www.foamfoamform.com/");
        configuration.addAllowedOrigin("https://foamfoamform.com/");
        configuration.addAllowedOrigin("http://localhost:3000/");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        configuration.addExposedHeader(JwtUtil.AUTHORIZATION_HEADER);
        configuration.addExposedHeader(JwtUtil.REFRESH_HEADER);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new CustomAuthenticationEntryPoint();
    }

}
