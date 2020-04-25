 package com.luv2code.springboot.thymeleafdemo.security;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@Configuration
public class CustomLoginSuccessHandler  extends SimpleUrlAuthenticationSuccessHandler{

	@Override
	protected void handle(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
	
	
		//we write a method for determineTargetUrl(authentication)
	String targetUrl = determineTargetUrl(authentication);
	
	if(response.isCommitted()) {
		return;
	}
	RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	redirectStrategy.sendRedirect(request, response, targetUrl);
	
	}
	
	
	protected String determineTargetUrl(Authentication authentication) {
		
		String url = "/login?error=true";
		
		//fetch the roles from authentication object
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		
		List<String> roles = new ArrayList<String>();
		
		for(GrantedAuthority a : authorities)
		{
			roles.add(a.getAuthority());
		}
		
		//check the user role and decide the redirect Url
		if(roles.contains("ADMIN_USER")) {
			 url ="/admin";
			 
			
		}
		else if(roles.contains("SITE_USER")) {
			url ="/customers/list";
		}
		return url;
		
	}
	
	

}
