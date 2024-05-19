package com.demo.parkingmanagementsystem.service;

import com.demo.parkingmanagementsystem.model.User;

public interface UserService {

	public User saveUser(User user);

	public User findUserByPhone(long phone);

}
