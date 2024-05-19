package com.demo.parkingmanagementsystem.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "PARKINGINFORMATION")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Parking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long parkingid;
	private String parkingname;
	private String parkingaddress;
	private String parkingowner;
	private long parkingownerphone;
	private String parkingmanager;
	private long parkingmanagerphone;
	private String parkingcity;
	private int twowheelertotalcount;
	private int twowheelerparkingrate;
	private int fourwheelertotalcount;
	private int fourwheelerparkingrate;

}
