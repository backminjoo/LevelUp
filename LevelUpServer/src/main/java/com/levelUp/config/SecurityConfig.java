package com.levelup.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import com.levelup.config.filter.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Autowired
	private JwtAuthenticationFilter jwtFilter;

	@Value("${host.url}")
	private String baseUrl;
	
	@Bean
	protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.cors(cors -> cors.configurationSource(request -> {
			CorsConfiguration config = new CorsConfiguration();
			config.setAllowedOrigins(Arrays.asList(baseUrl)); // 이 도메인 허용
			config.setAllowedHeaders(Arrays.asList("*")); // 모든 헤더를 허용
			config.setAllowedMethods(Arrays.asList("*")); // 모든 HTTP 메서드(POST, GET, PUT, DELETE 등)를 허용
			
			return config;
		})).csrf(csrf -> csrf.disable()) 
		.formLogin(form -> form.disable()).httpBasic(basic->basic.disable())
		.authorizeHttpRequests(request -> request
				// 로그인 - 모든 사용자 접근 가능
//		        .requestMatchers(HttpMethod.POST, "/auth").permitAll()
		        .requestMatchers("*").permitAll()
		        // 운영자 전용 기능 - ADMIN 접근 가능
		        .requestMatchers("/admin/**").hasRole("ADMIN")
		        // 기타 모든 요청은 인증된 사용자만 접근 가능
		        .anyRequest().authenticated())
			.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class); // UsernamePasswordAuthenticationFilter 이전에 필터를 추가

			return http.build();
		}
		
	@Bean
	protected PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder(); // sha512 대신 사용
	}
}