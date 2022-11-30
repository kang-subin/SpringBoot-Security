package com.hospital.review.configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity // spring security 활성화
@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity // 세부 보안 설정
                .httpBasic().disable()
                .csrf().disable()// user 가 아닌 다른 사람이 crud를 하는 것을 막아줌
                .cors().and()// 동일 출처에서만 허용..?
                .authorizeRequests()// 시큐리티 처리에 HttpServletRequest를 이용한다는 것을 의미
                .antMatchers("/api/**").permitAll() // 특정 url(리소스)에 대한 권한 설정
                .antMatchers("/api/v1/users/join", "/api/v1/users/login").permitAll() // antMatchers 설정한 리소스의 접근을 인증절차 없이 허용
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt사용하는 경우 씀
                .and()
                //     .addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class) //UserNamePasswordAuthenticationFilter적용하기 전에 JWTTokenFilter를 적용 하라는 뜻 입니다.
                .build();
    }

}

