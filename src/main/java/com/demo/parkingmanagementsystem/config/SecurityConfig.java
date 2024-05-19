package com.demo.parkingmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.demo.parkingmanagementsystem.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	
	/** 
	 * @return UserDetailsService
	 */
	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public AuthenticationSuccessHandler myAuthenticationSuccessHandler() {
		return new CustomAuthenticationSuccessHandler();
	}

	@Override
	protected void configure(HttpSecurity security) throws Exception {
		/*
		 * security.csrf().disable().authorizeRequests().antMatchers("/**").permitAll().
		 * anyRequest().authenticated().and()
		 * .formLogin().loginPage("/login").defaultSuccessUrl("/home").permitAll().and()
		 * .logout()
		 * .logoutSuccessUrl("/").invalidateHttpSession(true).clearAuthentication(true).
		 * permitAll();
		 */

		security.csrf().disable().authorizeRequests().antMatchers("/**").permitAll().anyRequest().authenticated().and()
				.formLogin().loginPage("/login").successHandler(myAuthenticationSuccessHandler()).permitAll().and()
				.logout().logoutSuccessUrl("/").invalidateHttpSession(true).clearAuthentication(true).permitAll();

	}
}
