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
@Table(name = "BOOKING")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long bookingid;
	private long userphone;
	private long parkingid;
	private String parkingcity;
	private String vehicletype;
	private String vehicleregistrationnumber;
	private String bookingdate;
	private String parkingstartdatetime;
	private String parkingenddatetime;
	private int totalparkinghours;
	private int billamount;
}
