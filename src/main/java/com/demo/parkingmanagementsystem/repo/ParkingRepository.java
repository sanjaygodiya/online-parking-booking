package com.demo.parkingmanagementsystem.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.demo.parkingmanagementsystem.model.Parking;

@Repository
public interface ParkingRepository extends JpaRepository<Parking, Long> {

	@Query(value ="SELECT * FROM PARKINGINFORMATION WHERE PARKINGCITY = ?1", nativeQuery = true)
	public List<Parking> getParkingListByCity(String city);
}
