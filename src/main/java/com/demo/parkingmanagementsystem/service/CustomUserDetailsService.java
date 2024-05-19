package com.demo.parkingmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.demo.parkingmanagementsystem.model.User;
import com.demo.parkingmanagementsystem.repo.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepo;

	
	/** 
	 * @param phone
	 * @return UserDetails
	 * @throws UsernameNotFoundException
	 */
	@Override
	public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {

		User user = userRepo.findByPhone(Long.parseLong(phone));
		if (user != null) {
			log.info("User logged in : " + user.getPhone());
			return new CustomUserDetails(user);
		} else {
			log.info("User not found with phone : " + phone);
			throw new UsernameNotFoundException("Invalid username or password.");
		}
	}

}
