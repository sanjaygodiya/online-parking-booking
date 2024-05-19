package com.demo.parkingmanagementsystem.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.parkingmanagementsystem.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	public User findByPhone(long phone);	

}
