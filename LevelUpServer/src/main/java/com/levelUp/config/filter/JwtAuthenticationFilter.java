package com.levelup.config.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.levelup.common.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtil jwt;
	
	private String extractToken(HttpServletRequest request) {
		String auth = request.getHeader("Authorization");
		if (auth != null && auth.startsWith("Bearer")) {
			return auth.substring(7);
		}
		return null;
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
	
		String token = extractToken(request);
		
		if(token != null && jwt.isVerified(token)) {
		
		String id = jwt.getSubject(token);
//		UserDetails member = mserv.loadUserByUsername(id); 
		
//		Authentication auth = new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
//		SecurityContextHolder.getContext().setAuthentication(auth);
		
		}
		filterChain.doFilter(request, response);
	}
}
