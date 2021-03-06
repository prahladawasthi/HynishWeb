package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.login.CustomerUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	CustomSuccessHandler customSuccessHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/bug*//**").permitAll().antMatchers("/static*//**").permitAll().antMatchers("/js*//**").permitAll()
				.antMatchers("/css*//**").permitAll().antMatchers("/resources*//**").permitAll().antMatchers("/reset")
				.permitAll().antMatchers("/common/forgot").permitAll().antMatchers("/register").permitAll()
				.antMatchers("/confirm").permitAll().antMatchers("/feedback/").permitAll().antMatchers("/").permitAll().antMatchers("/common/**")
				.hasAnyRole("ADMIN", "RESIDENTS", "MAINTENANCE").antMatchers("/admin/**").hasRole("ADMIN")
				.antMatchers("/user/**").hasAnyRole("ADMIN", "RESIDENTS").antMatchers("/maintenance/**")
				.hasAnyRole("ADMIN", "MAINTENANCE").anyRequest().authenticated().and().formLogin()
				.successHandler(customSuccessHandler).loginPage("/common/login").permitAll().and().logout().permitAll().and()
				.csrf().and().exceptionHandling().accessDeniedPage("/common/accessDenied");
	}

	@Bean
	public UserDetailsService mongoUserDetails() {
		return new CustomerUserDetailsService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		UserDetailsService userDetailsService = mongoUserDetails();
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(mongoUserDetails());
		authenticationProvider.setPasswordEncoder(passwordEncoder());
		return authenticationProvider;
	}

}