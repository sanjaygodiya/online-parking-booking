package com.demo.parkingmanagementsystem;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;

import com.demo.parkingmanagementsystem.model.User;
import com.demo.parkingmanagementsystem.repo.UserRepository;
import com.demo.parkingmanagementsystem.service.UserServiceImpl;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
@ActiveProfiles("dev")
public class UserRepositoryTests {

	@InjectMocks
	private UserServiceImpl userServiceImpl;

	@Mock
	private UserRepository userRep;

	@Autowired
	private UserRepository userRepo;

	static User user = new User();

	@BeforeAll
	public static void buildUser() {
		user.setPhone(1234);
		user.setFirstname("Unit");
		user.setLastname("Test1");
		user.setEmail("unittest1@mail.com");
		user.setCity("Bengaluru");
		user.setPassword("**********");
		user.setRole("TEST");
	}

	@Test
	@Order(1)
	public void createUserTest() {
		User savedUser = userRepo.save(user);
		assertEquals(savedUser.getPhone(), user.getPhone());
	}

	@Test
	@Order(2)
	public void getUserTest() {
		User findUser = userRepo.findByPhone(user.getPhone());
		assertEquals(findUser.getFirstname(), user.getFirstname());
		assertEquals(findUser.getLastname(), user.getLastname());
		assertEquals(findUser.getEmail(), user.getEmail());
		assertEquals(findUser.getRole(), user.getRole());
	}
}