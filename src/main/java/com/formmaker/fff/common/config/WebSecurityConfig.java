package com.formmaker.fff.common.config;

import com.formmaker.fff.common.jwt.JwtAuthFilter;
import com.formmaker.fff.common.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//
//        return (web) -> web.ignoring().requestMatchers(PathRequest.toH2Console());
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /* 1. CSRF(Cross-site request forgery) 비활성화 및 CORS 설정 */
        http.csrf().disable()
                .cors().configurationSource(corsConfigurationSource());

        /* 2. Session 비활성화 */
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        /* 3. Request에 대한 인증/인가 */
        http.authorizeRequests()
                .antMatchers("/api/user/**").permitAll()
                .antMatchers(HttpMethod.GET, "/api/survey").permitAll()
                .antMatchers(HttpMethod.GET,"/api/question").permitAll()
                .antMatchers(HttpMethod.GET, "/api/survey/**/reply").permitAll()
                .antMatchers(HttpMethod.GET, "/api/survey/main").permitAll()
                /*swagger*/
                .antMatchers("/v2/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .antMatchers("/swagger**").permitAll()

                .antMatchers("/swagger-resources/**").permitAll()


                /* 이후 sse관련 기능 사용시 */
//                antMatchers("/api/sse/**").permitAll().
                /* 3-1. Authentication 예외 처리 */
                .anyRequest().authenticated();

        http.addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // cors 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:3000");
//        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*"); // 허용할 Http Method
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true); // 내 서버가 응답할 때 json을 js에서 처리할 수 있게 설정
        configuration.setMaxAge(3600L);
        configuration.addExposedHeader(JwtUtil.AUTHORIZATION_HEADER);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}
