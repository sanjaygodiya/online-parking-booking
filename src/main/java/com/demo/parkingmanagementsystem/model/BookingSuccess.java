package com.demo.parkingmanagementsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingSuccess {
	
	private long bookingid;
	private long userphone;
	private String parkingname;
	private String parkingaddress;
	private String vehicletype;
	private String vehicleregistrationnumber;
	private String bookingdate;
	private String parkingstartdatetime;
	private String parkingenddatetime;
	private int totalparkinghours;
	private int billamount;
}
