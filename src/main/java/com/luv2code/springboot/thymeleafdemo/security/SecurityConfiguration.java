package com.luv2code.springboot.thymeleafdemo.security;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//we autowire this for authentication of the roles 
	//we have written this config class
	@Autowired
     private CustomLoginSuccessHandler successHandler;
	

	//we get this from spring directly, we just need to autowire it here
	//data source :establishes a connection with database..the location where data being uses
	//is coming from.
	@Autowired
	private DataSource dataSource;
	

	//these are the queries for authentication and authorization
	//we have written these in the applications.properties file
	@Value("${spring.queries.users-query}")
	private String usersQuery;

	@Value("${spring.queries.roles-query}")
	private String rolesQuery;

	
	//I'm using JDBC based authentication and authorization
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		//we are providing the datasource and authentication and authorization query
		//and the password encoder
		
		//this will be used when we are logging into the system.
		
		auth.jdbcAuthentication().usersByUsernameQuery(usersQuery)
		                         .authoritiesByUsernameQuery(rolesQuery)
				                 .dataSource(dataSource)
				                  .passwordEncoder(bCryptPasswordEncoder);
	}

	
	//This method, when we access a particular URL :  what happens when we access that
	//particular URL
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
				//for URLs matching for access rights
				.antMatchers("/").permitAll()
				.antMatchers("/login").permitAll()
				.antMatchers("/register").permitAll()
				.antMatchers("/customers/list").hasAnyAuthority("SUPER_USER", "ADMIN_USER", "SITE_USER")
				.antMatchers("/admin").hasAnyAuthority("SUPER_USER","ADMIN_USER")
				.anyRequest().authenticated()
				.and()
				// form login
				.csrf().disable().formLogin()
				.loginPage("/login")
				.failureUrl("/login?error=true")
				//.defaultSuccessUrl("/customers/list")
				//we autowire the customSucessHandler config class 
				.successHandler(successHandler)
				.usernameParameter("email")
				.passwordParameter("password")
				.and()
				// logout
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/").and()
				.exceptionHandling()
				.accessDeniedPage("/access-denied");
	}

	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/images/**");
	}

}