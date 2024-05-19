package com.demo.parkingmanagementsystem.service;

import java.util.List;

import com.demo.parkingmanagementsystem.model.Parking;

public interface ParkingService {

	public Parking saveParkingDetails(Parking parking);
	public List<Parking> getParkingList(String city);
	public Parking getParkingDetails(long id);
}
