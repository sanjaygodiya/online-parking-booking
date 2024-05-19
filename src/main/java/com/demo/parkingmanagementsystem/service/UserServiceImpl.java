package com.demo.parkingmanagementsystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.parkingmanagementsystem.model.User;
import com.demo.parkingmanagementsystem.repo.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	
	/** 
	 * @return PasswordEncoder
	 */
	@Bean
	public static PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	private UserRepository userRepo;

	public UserServiceImpl(UserRepository userRepo) {
		this.userRepo = userRepo;
	}

	
	/** 
	 * @param userDto
	 * @return User
	 */
	@Override
	@Transactional
	public User saveUser(User userDto) {
		User user = new User();
		user.setFirstname(userDto.getFirstname().trim());
		user.setLastname(userDto.getLastname().trim());
		user.setEmail(userDto.getEmail().trim());
		user.setPhone(userDto.getPhone());
		user.setCity(userDto.getCity().trim());
		user.setRole("USER");
		user.setPassword(passwordEncoder().encode(userDto.getPassword()));
		try {
			return userRepo.save(user);
		} catch (Exception e) {
			log.error("Exeption in User service : " + e.getMessage());
			return null;
		}

	}

	
	/** 
	 * @param phone
	 * @return User
	 */
	@Override
	@Transactional(readOnly = true)
	public User findUserByPhone(long phone) {
		log.info("User phone : "+phone);
		return userRepo.findByPhone(phone);
	}

}
