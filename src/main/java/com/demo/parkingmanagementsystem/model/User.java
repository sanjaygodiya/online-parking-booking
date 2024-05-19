package com.demo.parkingmanagementsystem.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "USERINFORMATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	private long phone;
	private String firstname;
	private String lastname;
	private String email;
	private String city;
	private String password;
	private String role;

}
